-- sql server only
-- this option is if persisted computed column and changing the schema is allowed.

-- add computed column and  indexes for performance
alter table HazelTree add lower as e11*1.0/e21 persisted;
alter table HazelTree add upper as e12*1.0/e22 persisted;

create nonclustered index idx_hazeltree_lower on HazelTree ( lower asc);
create nonclustered index idx_hazeltree_right on HazelTree ( upper asc);
