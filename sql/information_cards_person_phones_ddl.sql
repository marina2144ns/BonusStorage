USE [BonusStorage];
GO

CREATE TABLE [dbo].[InformationCards]
(
    [Id] BIGINT IDENTITY(1,1) NOT NULL,
    [CardId] BINARY(16) NOT NULL,
    [CardCode] NVARCHAR(200) NULL,
    [PersonId] BINARY(16) NULL,
    [CurrentVersion] BIGINT NOT NULL,

    CONSTRAINT [PK_InformationCards]
        PRIMARY KEY CLUSTERED ([Id])
);
GO

CREATE UNIQUE NONCLUSTERED INDEX [UX_InformationCards_CardId]
    ON [dbo].[InformationCards] ([CardId]);
GO

CREATE NONCLUSTERED INDEX [IX_InformationCards_CardCode]
    ON [dbo].[InformationCards] ([CardCode]);
GO

CREATE TABLE [dbo].[PersonPhones]
(
    [Id] BIGINT IDENTITY(1,1) NOT NULL,
    [PersonId] BINARY(16) NOT NULL,
    [Phone] NVARCHAR(100) NOT NULL,
    [CurrentVersion] BIGINT NOT NULL,

    CONSTRAINT [PK_PersonPhones]
        PRIMARY KEY CLUSTERED ([Id])
);
GO

CREATE NONCLUSTERED INDEX [IX_PersonPhones_PersonId]
    ON [dbo].[PersonPhones] ([PersonId]);
GO

CREATE NONCLUSTERED INDEX [IX_PersonPhones_Phone]
    ON [dbo].[PersonPhones] ([Phone]);
GO
