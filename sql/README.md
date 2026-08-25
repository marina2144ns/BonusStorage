# SQL schema

The SQL scripts define the `BonusStorage` database objects.

## DDL

- `ddl/documents.sql` — source configuration, document types, documents, bonus events, operation types, SMS notification data, foreign keys, and indexes.
- `ddl/information_cards.sql` — information cards and person phone data used by the card-and-phone data mart.

## Initial data

- `data/reference_data.sql` — initial source database, operation types, and document types.

## Stored procedures

- `procedures/upload_bonus_documents.sql` — loads changed documents from the configured retail source database into `dbo.Documents`.

For a new database, run the scripts in this order:

1. `ddl/documents.sql`
2. `ddl/information_cards.sql`
3. `data/reference_data.sql`
4. scripts from `procedures`

The DDL and initial-data files are creation scripts and intentionally fail if the objects or data already exist.
