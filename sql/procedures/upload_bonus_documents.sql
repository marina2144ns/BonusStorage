USE [BonusStorage];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE PROCEDURE [dbo].[UploadBonusDocuments]
AS
BEGIN
    SET NOCOUNT ON;

    -- Connection settings for the retail source database.
    DECLARE @retailBaseName VARCHAR(255);
    DECLARE @retailBaseConnectionString VARCHAR(1000);
    DECLARE @retailBaseId INT;

    SET @retailBaseName = 'retail_2017_stockmann';

    SELECT
        @retailBaseId = [Id],
        @retailBaseConnectionString = [ConnectString]
    FROM [dbo].[SourceBases]
    WHERE [Name] = @retailBaseName;

    -- The latest document version already loaded for a document type.
    DECLARE @maxVersion BIGINT;

    -- Iterate through configured document types.
    DECLARE @DocTypes_Id INT;

    SELECT @DocTypes_Id = MIN([Id])
    FROM [dbo].[DocumentTypes]
    WHERE [SourceBase] = @retailBaseId;

    DECLARE @QueryString VARCHAR(2000);

    DECLARE @DocCount INT;
    SET @DocCount = 0;

    DECLARE @MaxDocCount INT;
    SET @MaxDocCount = 10000000;

    DECLARE @TempCount INT;

    CREATE TABLE #t1
    (
        [SourceBase] INT NOT NULL,
        [DocumentType] INT NOT NULL,
        [Ext_IDRRef] BINARY(16) NOT NULL,
        [OneCId] CHAR(36) NOT NULL,
        [Ext_Date_Time] DATETIME2(0) NOT NULL,
        [Ext_Number] VARCHAR(50) NOT NULL,
        [CurrentVersion] BIGINT NOT NULL
    );

    DECLARE @CurrentExtId BINARY(16);
    SET @CurrentExtId = 0;

    WHILE @DocTypes_Id IS NOT NULL
    BEGIN
        SELECT @maxVersion = MAX([CurrentVersion])
        FROM [dbo].[Documents]
        WHERE [SourceBase] = @retailBaseId
          AND [DocumentType] = @DocTypes_Id;

        IF @maxVersion IS NULL
            SET @maxVersion = 0;

        SELECT @QueryString = CONCAT(
            'SELECT TOP ',
            @MaxDocCount - @DocCount,
            ' ',
            @retailBaseId,
            ' SourceBase, ',
            [Id],
            ' DocumentType, ',
            ' _IDRRef Ext_IDRRef,',
            ' CONCAT(SUBSTRING(CONVERT(char(36),_IDRRef,2),25,8),''-'',',
            ' SUBSTRING(CONVERT(char(36),_IDRRef,2),21,4),''-'',',
            ' SUBSTRING(CONVERT(char(36),_IDRRef,2),17,4),''-'',',
            ' SUBSTRING(CONVERT(char(36),_IDRRef,2),1,4),''-'',',
            ' SUBSTRING(CONVERT(char(36),_IDRRef,2),5,12)) OneCId,',
            ' _Date_Time Ext_Date_Time,',
            ' _Number Ext_Number,',
            ' CAST(_Version AS BIGINT) CurrentVersion',
            ' FROM ',
            @retailBaseConnectionString,
            [SourceTable],
            ' WHERE _Version > ',
            @maxVersion,
            ' ORDER BY _Version'
        )
        FROM [dbo].[DocumentTypes]
        WHERE [Id] = @DocTypes_Id;

        SELECT @DocTypes_Id = MIN([Id])
        FROM [dbo].[DocumentTypes]
        WHERE [Id] > @DocTypes_Id;

        INSERT INTO #t1
        EXEC (@QueryString);

        SET @TempCount = (SELECT COUNT(*) FROM #t1);
        SET @DocCount = @DocCount + @TempCount;

        IF @DocCount > @MaxDocCount
            BREAK;
        ELSE
            CONTINUE;
    END;

    INSERT INTO [dbo].[Documents]
    (
        [SourceBase],
        [DocumentType],
        [Ext_IDRRef],
        [OneCId],
        [Ext_Date_Time],
        [Ext_Number],
        [CurrentVersion],
        [IsChanged],
        [Created]
    )
    SELECT
        t1.[SourceBase],
        t1.[DocumentType],
        t1.[Ext_IDRRef],
        t1.[OneCId],
        t1.[Ext_Date_Time],
        t1.[Ext_Number],
        t1.[CurrentVersion],
        1,
        SYSDATETIME()
    FROM #t1 AS t1
    LEFT JOIN [dbo].[Documents] AS d
        ON t1.[SourceBase] = d.[SourceBase]
       AND t1.[DocumentType] = d.[DocumentType]
       AND t1.[Ext_IDRRef] = d.[Ext_IDRRef]
    WHERE d.[Ext_IDRRef] IS NULL;

    UPDATE d
    SET
        d.[SourceBase] = t1.[SourceBase],
        d.[DocumentType] = t1.[DocumentType],
        d.[Ext_IDRRef] = t1.[Ext_IDRRef],
        d.[OneCId] = t1.[OneCId],
        d.[Ext_Date_Time] = t1.[Ext_Date_Time],
        d.[Ext_Number] = t1.[Ext_Number],
        d.[CurrentVersion] = t1.[CurrentVersion],
        d.[IsChanged] = 1,
        d.[Updated] = SYSDATETIME()
    FROM [dbo].[Documents] AS d
    JOIN #t1 AS t1
        ON t1.[SourceBase] = d.[SourceBase]
       AND t1.[DocumentType] = d.[DocumentType]
       AND t1.[Ext_IDRRef] = d.[Ext_IDRRef]
    WHERE t1.[CurrentVersion] <> d.[CurrentVersion];

    DROP TABLE #t1;
END;
GO
