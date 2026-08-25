USE [BonusStorage];
GO

SET NOCOUNT ON;
GO

DECLARE @SourceBaseId INT;

INSERT INTO [dbo].[SourceBases]
(
    [Name],
    [ConnectString],
    [CurrentVersionBonusesProcessed]
)
VALUES
(
    'retail_2017_stockmann',
    'DB01001.retail_2017_stockmann.[dbo].',
    0
);

SET @SourceBaseId = CONVERT(INT, SCOPE_IDENTITY());

INSERT INTO [dbo].[OperationTypes] ([Name])
VALUES
    ('Списание бонусов за покупку в универмаге'),
    ('Начисление бонусов за покупку в универмаге'),
    ('Списание бонусов за оформление заказа'),
    ('Начисление бонусов за выкуп заказа'),
    ('Возврат списанных бонусов за возврат товара, купленного в универмаге'),
    ('Возврат списанных бонусов за возврат товара по заказу'),
    ('Списание начисленных бонусов за возврат товара, купленного в универмаге'),
    ('Списание начисленных бонусов за возврат товара по заказу'),
    ('Подарочные бонусные баллы'),
    ('Ручная корректировка'),
    ('Списание бонусных баллов при отсутствии продаж');

INSERT INTO [dbo].[DocumentTypes]
(
    [SourceBase],
    [DocumentName],
    [SourceTable]
)
VALUES
    (@SourceBaseId, 'ЧекККМ', '_Document223'),
    (@SourceBaseId, 'РеализацияТоваров', '_Document203'),
    (@SourceBaseId, 'ОтчетОРозничныхПродажах', '_Document185'),
    (@SourceBaseId, 'НачислениеИСписаниеБонусныхБаллов', '_Document177'),
    (@SourceBaseId, 'КорректировкаРегистров', '_Document174'),
    (@SourceBaseId, 'ЗаказПокупателя', '_Document164'),
    (@SourceBaseId, 'ВозвратТоваровОтПокупателя', '_Document158');
GO
