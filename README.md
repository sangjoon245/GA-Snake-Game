# GA-Snake-Game
A Snake Game simulation, optimizing its score using the Genetic Algorithm.

Using a fitness function that depends heavily on getting the food, with a side reward value for surviving, some number of snake runs are selected. When those snakes are selected, we use a roulette function that selects two more snakes out of those selected runs to create a "child" snake. This two-selection process heavily favors the snakes with higher snake-values. We will continue this until we create enough "children" snake to run this code again.
