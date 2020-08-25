-- must uniquely identify a node
declare @name nvarchar(64);
set @name = '33';

/* 1. recursive CTE */
set statistics time on;

with _CTE (name, parent, number, e11, e21, e12, e22, depth)
as	(	
	select name, parent, number, e11, e21, e12, e22, 1 from HazelTreeSmall 
	-- predicate uniquely identifying a node as root
	where name = @name  
		
	union all
	select child.name, child.parent, child.number, child.e11, child.e21, child.e12, child.e22, cte.depth+1 from _CTE cte
	inner join HazelTree child on (cte.name = child.parent)
	)
select name, number, e11/e21*1.0 as lft, e12/e22*1.0 as rgt, depth 
from _CTE 
-- limit recursion to avoid infinite recursion
option(MAXRECURSION 50); 

set statistics time off;

/* 2. exact matrix query */
set statistics time on;

select child.*
from HazelTree child, HazelTree node   
where node.e11*child.e21 <= child.e11*node.e21
and child.e12*node.e22 <= node.e12*child.e22 
-- predicate uniquely identifying a node 
and node.name = @name  

set statistics time off;

/* 3a. matrix query with computed indexed column */
set statistics time on;

select child.*
from HazelTree child, HazelTree node 
-- approximate for indexing
where child.lower
between node.lower and node.upper
-- exact
and node.e11*child.e21 <= child.e11*node.e21 
and child.e12*node.e22 <= node.e12*child.e22 
-- predicate uniquely identifying a node 
and node.name = @name ;

set statistics time off;

/* 3b. matrix query with computed indexed view */
set statistics time on;

select child.*
from HazelTreeView child, HazelTreeView node 
-- approximate for indexing
where child.lower
between node.lower and node.upper
-- exact
and node.e11*child.e21 <= child.e11*node.e21 
and child.e12*node.e22 <= node.e12*child.e22 
-- predicate uniquely identifying a node 
and node.name = @name ;

set statistics time off;