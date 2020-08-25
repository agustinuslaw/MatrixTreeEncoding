-- sql server only
-- this option is to avoid altering schema

drop view if exists dbo.HazelTreeView ;
go

create view dbo.HazelTreeView
	with schemabinding
	as  
		-- note that *1.0 is necessary to convert bigint to numeric
		select name, e11, e21, e12, e22, (e11*1.0/e21) as [lower], (e12*1.0/e22) as [upper] 
		from dbo.HazelTree;
go

create unique clustered index idx_hazeltreeview_name on dbo.HazelTreeView (name);
create nonclustered index idx_hazeltreeview_lower on dbo.HazelTreeView ( [lower] asc);
create nonclustered index idx_hazeltreeview_upper on dbo.HazelTreeView ( [upper] asc);
go