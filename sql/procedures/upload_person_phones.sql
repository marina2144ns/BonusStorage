USE [BonusStorage];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE PROCEDURE [dbo].[UploadPersonPhones]
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE @retailBaseName VARCHAR(255);
    DECLARE @retailBaseConnectionString VARCHAR(1000);
    DECLARE @maxVersion BIGINT;
    DECLARE @QueryString NVARCHAR(MAX);
    DECLARE @DeletedCount INT;
    DECLARE @InsertedCount INT;

    SET @retailBaseName = 'retail_2017_stockmann';

    SELECT
        @retailBaseConnectionString = [ConnectString]
    FROM [dbo].[SourceBases]
    WHERE [Name] = @retailBaseName;

    IF @retailBaseConnectionString IS NULL
    BEGIN
        RAISERROR(
            'Source database retail_2017_stockmann is not configured.',
            16,
            1
        );
        RETURN;
    END;

    SELECT @maxVersion = MAX([CurrentVersion])
    FROM [dbo].[PersonPhones];

    IF @maxVersion IS NULL
        SET @maxVersion = 0;

    CREATE TABLE #ChangedPersonPhones
    (
        [PersonId] BINARY(16) NOT NULL,
        [Phone] NVARCHAR(100) NULL,
        [CurrentVersion] BIGINT NOT NULL
    );

    SET @QueryString = CONCAT(
        'SELECT ',
        '    person._IDRRef AS PersonId, ',
        '    contact._Fld2619 AS Phone, ',
        '    CAST(person._Version AS BIGINT) AS CurrentVersion ',
        'FROM ',
        @retailBaseConnectionString,
        '_Reference139 AS person ',
        'LEFT JOIN ',
        @retailBaseConnectionString,
        '_Reference139_VT2615 AS contact ',
        '    ON contact._Fld438 = 0 ',
        '   AND contact._Fld2617RRef = ',
        '       0xA95FD3E53F7B10FB46F57B9225F4B5FE ',
        '   AND person._IDRRef = contact._Reference139_IDRRef ',
        'WHERE person._Fld438 = 0 ',
        '  AND CAST(person._Version AS BIGINT) > ',
        @maxVersion,
        ' ORDER BY person._Version'
    );

    INSERT INTO #ChangedPersonPhones
    (
        [PersonId],
        [Phone],
        [CurrentVersion]
    )
    EXEC (@QueryString);

    BEGIN TRY
        BEGIN TRANSACTION;

        DELETE target
        FROM [dbo].[PersonPhones] AS target
        WHERE EXISTS
        (
            SELECT 1
            FROM #ChangedPersonPhones AS source
            WHERE source.[PersonId] = target.[PersonId]
        );

        SET @DeletedCount = @@ROWCOUNT;

        INSERT INTO [dbo].[PersonPhones]
        (
            [PersonId],
            [Phone],
            [CurrentVersion]
        )
        SELECT
            source.[PersonId],
            source.[Phone],
            source.[CurrentVersion]
        FROM #ChangedPersonPhones AS source
        WHERE source.[Phone] IS NOT NULL;

        SET @InsertedCount = @@ROWCOUNT;

        COMMIT TRANSACTION;

        SELECT
            @DeletedCount AS [DeletedCount],
            @InsertedCount AS [InsertedCount],
            COUNT(*) AS [SourceRows]
        FROM #ChangedPersonPhones;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        THROW;
    END CATCH;
END;
GO
