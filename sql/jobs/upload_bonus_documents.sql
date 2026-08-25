USE [msdb];
GO

SET NOCOUNT ON;
SET XACT_ABORT ON;
GO

IF EXISTS
(
    SELECT 1
    FROM [msdb].[dbo].[sysjobs]
    WHERE [name] = N'UploadBonusDocumentsRegl'
)
BEGIN
    RAISERROR(
        'SQL Agent Job UploadBonusDocumentsRegl already exists.',
        16,
        1
    );
    RETURN;
END;
GO

BEGIN TRY
    BEGIN TRANSACTION;

    DECLARE @ReturnCode INT;
    DECLARE @jobId BINARY(16);
    DECLARE @ownerLoginName SYSNAME;

    SET @ReturnCode = 0;
    SET @ownerLoginName = SUSER_SNAME();

    IF NOT EXISTS
    (
        SELECT 1
        FROM [msdb].[dbo].[syscategories]
        WHERE [name] = N'[Uncategorized (Local)]'
          AND [category_class] = 1
    )
    BEGIN
        EXEC @ReturnCode = [msdb].[dbo].[sp_add_category]
            @class = N'JOB',
            @type = N'LOCAL',
            @name = N'[Uncategorized (Local)]';

        IF @ReturnCode <> 0
            RAISERROR('Could not create SQL Agent Job category.', 16, 1);
    END;

    EXEC @ReturnCode = [msdb].[dbo].[sp_add_job]
        @job_name = N'UploadBonusDocumentsRegl',
        @enabled = 1,
        @notify_level_eventlog = 0,
        @notify_level_email = 0,
        @notify_level_netsend = 0,
        @notify_level_page = 0,
        @delete_level = 0,
        @description = N'Loads changed bonus documents into BonusStorage.',
        @category_name = N'[Uncategorized (Local)]',
        @owner_login_name = @ownerLoginName,
        @job_id = @jobId OUTPUT;

    IF @ReturnCode <> 0
        RAISERROR('Could not create SQL Agent Job.', 16, 1);

    EXEC @ReturnCode = [msdb].[dbo].[sp_add_jobstep]
        @job_id = @jobId,
        @step_name = N'Upload bonus documents',
        @step_id = 1,
        @cmdexec_success_code = 0,
        @on_success_action = 1,
        @on_success_step_id = 0,
        @on_fail_action = 2,
        @on_fail_step_id = 0,
        @retry_attempts = 0,
        @retry_interval = 0,
        @os_run_priority = 0,
        @subsystem = N'TSQL',
        @command = N'EXEC [dbo].[UploadBonusDocuments];',
        @database_name = N'BonusStorage',
        @flags = 0;

    IF @ReturnCode <> 0
        RAISERROR('Could not create SQL Agent Job step.', 16, 1);

    EXEC @ReturnCode = [msdb].[dbo].[sp_update_job]
        @job_id = @jobId,
        @start_step_id = 1;

    IF @ReturnCode <> 0
        RAISERROR('Could not set the initial SQL Agent Job step.', 16, 1);

    EXEC @ReturnCode = [msdb].[dbo].[sp_add_jobschedule]
        @job_id = @jobId,
        @name = N'Запуск каждые 10 секунд',
        @enabled = 1,
        @freq_type = 4,
        @freq_interval = 1,
        @freq_subday_type = 2,
        @freq_subday_interval = 10,
        @freq_relative_interval = 0,
        @freq_recurrence_factor = 0,
        @active_start_date = 20240521,
        @active_end_date = 99991231,
        @active_start_time = 0,
        @active_end_time = 235959;

    IF @ReturnCode <> 0
        RAISERROR('Could not create SQL Agent Job schedule.', 16, 1);

    EXEC @ReturnCode = [msdb].[dbo].[sp_add_jobserver]
        @job_id = @jobId,
        @server_name = N'(local)';

    IF @ReturnCode <> 0
        RAISERROR('Could not assign SQL Agent Job to the local server.', 16, 1);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;

    THROW;
END CATCH;
GO
