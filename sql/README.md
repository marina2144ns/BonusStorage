# SQL scripts

The DDL scripts are creation scripts for an empty `BonusStorage` database. Run them in filename order:

1. `ddl/01_existing_tables.sql` — existing project tables and their foreign keys.
2. `ddl/02_information_cards_person_phones.sql` — information-card and person-phone tables for the new data mart.

The scripts intentionally fail if the objects already exist. They are schema snapshots, not repeatable migrations.
