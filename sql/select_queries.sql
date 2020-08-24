-- must uniquely identify a node
declare @name nvarchar(64);
set @name = '33';

/* 1. recursive CTE */
set statistics time on;
with _CTE (name, parent, number, e11, e21, e12, e22, depth)
as	(	
	select name, parent, number, e11, e21, e12, e22, 1 from HazelTreeSmall 
	where name = @name  -- predicate uniquely identifying a node as root
		
	union all
	select child.name, child.parent, child.number, child.e11, child.e21, child.e12, child.e22, cte.depth+1 from _CTE cte
	inner join HazelTreeSmall child on (cte.name = child.parent)
	)
select name, number, e11/e21*1.0 as lft, e12/e22*1.0 as rgt, depth 
from _CTE 
option(MAXRECURSION 50); -- limit recursion to avoid infinite recursion
set statistics time off;

/* 2. single query */
set statistics time on;

select child.*, child.e11/child.e21*1.0 as lft, child.e12/child.e22*1.0 as rgt
from HazelTreeSmall child, HazelTreeSmall node
where node.e11/node.e21*1.0 /*root.lft*/ <= child.e11/child.e21*1.0 /*lft*/ 
and child.e12/child.e22*1.0 /*rgt*/ <= node.e12/node.e22*1.0 /*root.rgt*/
and node.name = @name  -- predicate uniquely identifying a node 

set statistics time off;
