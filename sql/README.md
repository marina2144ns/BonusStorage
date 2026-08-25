# SQL schema

The SQL scripts define the `BonusStorage` database objects.

## DDL

- `ddl/documents.sql` — source configuration, document types, documents, bonus events, operation types, and SMS notification data.
- `ddl/information_cards.sql` — information cards and person phone data used by the card-and-phone data mart.

For a new database, run `documents.sql` first and then `information_cards.sql`.

## Stored procedures

- `procedures/upload_bonus_documents.sql` — loads changed documents from the configured retail source database into `dbo.Documents`.

The DDL files are schema creation snapshots and intentionally fail if the objects already exist.
