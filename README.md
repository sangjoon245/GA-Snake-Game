# GA-Snake-Game
A Snake Game simulation, optimizing its score using the Genetic Algorithm.

Using a fitness function that depends heavily on getting the food, with a side reward value for surviving, 100 number of snake runs are selected. When those snakes are selected, we use a roulette function that selects two more snakes out of those selected runs to create a "child" snake. This two-selection process heavily favors the snakes with higher snake-values. We will continue this until we create enough "children" snake to run this code again.

This snake takes in 24 inputs. It checks 8 directions (2 horizonal, 2 vertical, and 4 diagonal), and in each direction, it takes in information of distance from self to the wall, food (if any), and body. So 3 x 8 = 24.

For this particular run, there were 2 hidden layers, each with 8 nodes each. Then there were 4 output nodes (up, down, left right).

In summary, using 2000 snakes per "generation", 100 are selected, and then 2000 more children are created using a mutation rate of 4% and a cross rate of 30%.
