
CREATE KEYSPACE fancyBookStore WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 2 };
USE fancyBookStore;