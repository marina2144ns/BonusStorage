USE [BonusStorage];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[SourceBases]
(
    [Id] INT IDENTITY(1,1) NOT NULL,
    [Name] VARCHAR(255) NOT NULL,
    [ConnectString] VARCHAR(1000) NULL,
    [CurrentVersionBonusesProcessed] BIGINT NULL,

    CONSTRAINT [PK_SourceBases]
        PRIMARY KEY CLUSTERED ([Id])
);
GO

CREATE TABLE [dbo].[OperationTypes]
(
    [Id] INT IDENTITY(1,1) NOT NULL,
    [Name] VARCHAR(500) NOT NULL,

    CONSTRAINT [PK_OperationTypes]
        PRIMARY KEY CLUSTERED ([Id])
);
GO

CREATE TABLE [dbo].[DocumentTypes]
(
    [Id] INT IDENTITY(1,1) NOT NULL,
    [SourceBase] INT NOT NULL,
    [DocumentName] VARCHAR(255) NOT NULL,
    [SourceTable] VARCHAR(255) NOT NULL,

    CONSTRAINT [PK_DocumentTypes]
        PRIMARY KEY CLUSTERED ([Id]),

    CONSTRAINT [FK_DocumentTypes_SourceBase]
        FOREIGN KEY ([SourceBase])
        REFERENCES [dbo].[SourceBases] ([Id])
);
GO

CREATE TABLE [dbo].[Documents]
(
    [Id] INT IDENTITY(1,1) NOT NULL,
    [SourceBase] INT NOT NULL,
    [DocumentType] INT NOT NULL,
    [Ext_IDRRef] BINARY(16) NOT NULL,
    [OneCId] CHAR(36) NULL,
    [Ext_Date_Time] DATETIME2(0) NOT NULL,
    [Ext_Number] VARCHAR(50) NOT NULL,
    [CurrentVersion] BIGINT NOT NULL,
    [IsChanged] BIT NOT NULL,
    [Created] DATETIME2(0) NULL,
    [Updated] DATETIME2(0) NULL,
    [BonusesUploaded] DATETIME2(0) NULL,

    CONSTRAINT [PK_Documents]
        PRIMARY KEY CLUSTERED ([Id]),

    CONSTRAINT [FK_Documents_DocumentType]
        FOREIGN KEY ([DocumentType])
        REFERENCES [dbo].[DocumentTypes] ([Id]),

    CONSTRAINT [FK_Documents_SourceBase]
        FOREIGN KEY ([SourceBase])
        REFERENCES [dbo].[SourceBases] ([Id])
);
GO

CREATE TABLE [dbo].[BonusesInDocuments]
(
    [Id] INT IDENTITY(1,1) NOT NULL,
    [SourceBase] INT NOT NULL,
    [DocumentType] INT NOT NULL,
    [Document] INT NOT NULL,
    [StoreId] BINARY(16) NULL,
    [StoreName] VARCHAR(100) NULL,
    [CardNumber] VARCHAR(100) NOT NULL,
    [TypeOfIncrement] VARCHAR(10) NOT NULL,
    [Value] FLOAT NOT NULL,
    [TypeOfOperation] INT NOT NULL,
    [TextOperation] VARCHAR(500) NULL,
    [OrderID] VARCHAR(50) NULL,
    [BonusStartDate] DATETIME2(0) NOT NULL,
    [BonusEndDate] DATETIME2(0) NULL,
    [Created] DATETIME2(0) NULL,

    CONSTRAINT [PK_BonusesInDocuments]
        PRIMARY KEY CLUSTERED ([Id]),

    CONSTRAINT [FK_BonusesInDocuments_Document]
        FOREIGN KEY ([Document])
        REFERENCES [dbo].[Documents] ([Id]),

    CONSTRAINT [FK_BonusesInDocuments_DocumentType]
        FOREIGN KEY ([DocumentType])
        REFERENCES [dbo].[DocumentTypes] ([Id]),

    CONSTRAINT [FK_BonusesInDocuments_SourceBase]
        FOREIGN KEY ([SourceBase])
        REFERENCES [dbo].[SourceBases] ([Id])
);
GO

CREATE TABLE [dbo].[SMS_informed]
(
    [Id] INT IDENTITY(1,1) NOT NULL,
    [Document] INT NOT NULL,
    [CurrentVersion] BIGINT NOT NULL,
    [EventsCount] BIGINT NOT NULL,
    [InformedAt] DATETIME2(0) NOT NULL
        CONSTRAINT [DF_SMS_informed_InformedAt]
        DEFAULT (SYSDATETIME()),

    CONSTRAINT [PK_SMS_informed]
        PRIMARY KEY CLUSTERED ([Id]),

    CONSTRAINT [FK_SMS_informed_Document]
        FOREIGN KEY ([Document])
        REFERENCES [dbo].[Documents] ([Id])
);
GO
