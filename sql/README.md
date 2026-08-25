# SQL schema

The DDL scripts create the `BonusStorage` database objects:

- `ddl/documents.sql` — source configuration, document types, documents, bonus events, operation types, and SMS notification data.
- `ddl/information_cards.sql` — information cards and person phone data used by the card-and-phone data mart.

For a new database, run `documents.sql` first and then `information_cards.sql`.

The scripts are schema creation snapshots and intentionally fail if the objects already exist.
