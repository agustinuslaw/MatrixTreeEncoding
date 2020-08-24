create table HazelTree (	/* Note we need length 140 name for the worst case number 2^27   */	name nvarchar(150) primary key,-- name containing the path 	parent nvarchar(150), -- for traditional recursive search comparison 	number int not null, -- node element		/* matrix tree encoding */    e11 bigint not null,    e21 bigint not null,    e12 bigint not null,    e22 bigint not null,    );-- add computed column and  indexes for performancealter table HazelTree add lower_bound as e11/e21 persisted;alter table HazelTree add upper_bound as e12/e22 persisted;create nonclustered index idx_hazeltree_lower on HazelTree ( lower_bound asc);create nonclustered index idx_hazeltree_right on HazelTree ( upper_bound asc);