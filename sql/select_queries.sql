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

/* 2. matrix query */
set statistics time on;

select child.*
from HazelTreeSmall child, HazelTreeSmall node   
where node.e11*child.e21 <= child.e11*node.e21
and child.e12*node.e22 <= node.e12*child.e22 
and node.name = @name  -- predicate uniquely identifying a node 

set statistics time off;

/* 3. matrix query with pre filter (no indexing needed) */
set statistics time on;

select child.*
from %1$s child, %1$s node 
-- approximate 
where child.e11/child.e21  
between node.e11/node.e21 and node.e12/node.e22
-- exact
and node.e11*child.e21 <= child.e11*node.e21 
and child.e12*node.e22 <= node.e12*child.e22 
-- predicate uniquely identifying a node 
and node.name = '%2$s';

set statistics time off;

/* 4. matrix query with computed indexed column */
set statistics time on;

select child.*
from %1$s child, %1$s node 
-- approximate for indexing
where child.lower_bound
between node.lower_bound and node.upper_bound
-- exact
and node.e11*child.e21 <= child.e11*node.e21 
and child.e12*node.e22 <= node.e12*child.e22 
-- predicate uniquely identifying a node 
and node.name = '%2$s';

set statistics time off;