/*
	Matrix
	e11	e12
	e21	e22
*/

CREATE TABLE HazelNonIndexedTree (
	name nvarchar(50) unique not null, 
	path_desc nvarchar(50),
    id	bigint identity primary key,
    e11 bigint not null,
    e21 bigint not null,
    e12 bigint not null,
    e22 bigint not null
);

CREATE TABLE HazelIndexedTree (
	name nvarchar(50) unique not null, 
	path_desc nvarchar(50),
    id	bigint identity primary key,
    e11 bigint not null,
    e21 bigint not null,
    e12 bigint not null,
    e22 bigint not null,
	left_bound as (e11*1.0/e21) persisted,
	right_bound as (e12*1.0/e22) persisted
);

create nonclustered index idx_hazel_left on HazelIndexedTree( left_bound asc);
go

create nonclustered index idx_hazel_right on HazelIndexedTree (right_bound asc);
go