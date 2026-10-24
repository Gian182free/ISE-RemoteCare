% STRIPS Planning Engine for RemoteCare

plan(State, Goals, []) :-
    satisfied(State, Goals).

plan(State, Goals, [Action | RestOfPlan]) :-
    select_goal(Goals, Goal, RemainingGoals),
    achieves(Action, Goal),
    can_apply(Action, State),
    apply_action(Action, State, NextState),
    plan(NextState, RemainingGoals, RestOfPlan).

% Check if all goals are satisfied in the current state
satisfied(State, Goals) :-
    subset(Goals, State).

% Select a goal from the list
select_goal([Goal | Rest], Goal, Rest).

% Define achieves relation between action and goal
achieves(Action, Goal) :-
    action(Action, _, AddList, _),
    member(Goal, AddList).

% Check if an action is applicable in the current state
can_apply(Action, State) :-
    action(Action, Preconditions, _, _),
    subset(Preconditions, State).

% Apply action: update state by removing DeleteList and adding AddList
apply_action(Action, State, NextState) :-
    action(Action, _, AddList, DeleteList),
    subtract_list(State, DeleteList, TempState),
    union_list(TempState, AddList, NextState).

% Subset utility predicate
subset([], _).
subset([H | T], List) :-
    member(H, List),
    subset(T, List).

% Safe list subtraction for tuProlog compatibility
subtract_list([], _, []).
subtract_list([H | T], ListToSubtract, Result) :-
    member(H, ListToSubtract), !,
    subtract_list(T, ListToSubtract, Result).
subtract_list([H | T], ListToSubtract, [H | Result]) :-
    subtract_list(T, ListToSubtract, Result).

% Safe list union for tuProlog compatibility
union_list([], L, L).
union_list([H | T], L, Result) :-
    member(H, L), !,
    union_list(T, L, Result).
union_list([H | T], L, [H | Result]) :-
    union_list(T, L, Result).