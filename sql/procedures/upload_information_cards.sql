USE [BonusStorage];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE PROCEDURE [dbo].[UploadInformationCards]
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE @retailBaseName VARCHAR(255);
    DECLARE @retailBaseConnectionString VARCHAR(1000);
    DECLARE @maxVersion BIGINT;
    DECLARE @QueryString NVARCHAR(MAX);
    DECLARE @InsertedCount INT;
    DECLARE @UpdatedCount INT;

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
    FROM [dbo].[InformationCards];

    IF @maxVersion IS NULL
        SET @maxVersion = 0;

    CREATE TABLE #ChangedInformationCards
    (
        [CardId] BINARY(16) NOT NULL,
        [CardCode] NVARCHAR(200) NULL,
        [PersonId] BINARY(16) NULL,
        [CurrentVersion] BIGINT NOT NULL
    );

    SET @QueryString = CONCAT(
        'SELECT ',
        '    _IDRRef AS CardId, ',
        '    _Fld1177 AS CardCode, ',
        '    _Fld1178_RRRef AS PersonId, ',
        '    CAST(_Version AS BIGINT) AS CurrentVersion ',
        'FROM ',
        @retailBaseConnectionString,
        '_Reference61 ',
        'WHERE _Fld1178_RTRef = 0x0000008B ',
        '  AND _Fld1178_TYPE = 0x08 ',
        '  AND CAST(_Version AS BIGINT) > ',
        @maxVersion,
        ' ORDER BY _Version'
    );

    INSERT INTO #ChangedInformationCards
    (
        [CardId],
        [CardCode],
        [PersonId],
        [CurrentVersion]
    )
    EXEC (@QueryString);

    BEGIN TRY
        BEGIN TRANSACTION;

        UPDATE target
        SET
            target.[CardCode] = source.[CardCode],
            target.[PersonId] = source.[PersonId],
            target.[CurrentVersion] = source.[CurrentVersion]
        FROM [dbo].[InformationCards] AS target
        JOIN #ChangedInformationCards AS source
            ON source.[CardId] = target.[CardId]
        WHERE source.[CurrentVersion] <> target.[CurrentVersion];

        SET @UpdatedCount = @@ROWCOUNT;

        INSERT INTO [dbo].[InformationCards]
        (
            [CardId],
            [CardCode],
            [PersonId],
            [CurrentVersion]
        )
        SELECT
            source.[CardId],
            source.[CardCode],
            source.[PersonId],
            source.[CurrentVersion]
        FROM #ChangedInformationCards AS source
        WHERE NOT EXISTS
        (
            SELECT 1
            FROM [dbo].[InformationCards] AS target
            WHERE target.[CardId] = source.[CardId]
        );

        SET @InsertedCount = @@ROWCOUNT;

        COMMIT TRANSACTION;

        SELECT
            @InsertedCount AS [InsertedCount],
            @UpdatedCount AS [UpdatedCount],
            COUNT(*) AS [SourceRows]
        FROM #ChangedInformationCards;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        THROW;
    END CATCH;
END;
GO
