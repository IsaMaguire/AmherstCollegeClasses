# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util
import numpy as np
import math

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        food = currentGameState.getFood()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        # print()
        # print("SGS" ,successorGameState)
        # print("NP", newPos)
        # print("NF", newFood)
        # print("NGS", newGhostStates)
        # print("NST", newScaredTimes)
        # print()

        # Get the distance to the closest ghost
        ghostPositions = [x.getPosition() for x in newGhostStates]
        closestGhostDistance = min([euclideanDist(x,newPos) for x in ghostPositions])
        
        # Get the number of food left, and distance to closest food
        numFood = newFood.count(True)
        closestFoodDistance = min([manhattanDist(i,newPos) for i, x in np.ndenumerate(list(newFood)) if x]) if numFood else 0
        

        if closestGhostDistance < 1:
            return -math.inf
        elif food[newPos[0]][newPos[1]]: 
            return math.inf
        else:
            return 1/closestFoodDistance
    
def euclideanDist(xy1,xy2):
    return ( (xy1[0] - xy2[0]) ** 2 + (xy1[1] - xy2[1]) ** 2 ) ** 0.5

def manhattanDist(xy1,xy2):
    return abs(xy1[0] - xy2[0]) + abs(xy1[1] - xy2[1])

def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        return self.miniMax(gameState,0)[0]
    
    def miniMax(self,gameState: GameState,count):
        agentNum = count % gameState.getNumAgents()

        if gameState.isWin() or gameState.isLose() or count >= self.depth * gameState.getNumAgents():
            return None,self.evaluationFunction(gameState)
        
        if agentNum == 0:
            x, maxAction = -math.inf, None
            for action in gameState.getLegalActions(agentNum):
                util = self.miniMax(gameState.generateSuccessor(agentNum,action),count+1)[1]
                if util > x:
                    maxAction, x = action, util
            return maxAction, x
        else:
            x, minAction = math.inf, None
            for action in gameState.getLegalActions(agentNum):
                util = self.miniMax(gameState.generateSuccessor(agentNum,action),count+1)[1]
                if util < x:
                    minAction, x = action, util
            return minAction, x



    


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        # Psuedocode from class only had us call maxValue, but to account for 
        # multiple opponents we need the abPruning method
        return self.abPruning(gameState,-math.inf,math.inf,0)[0]
    
    def abPruning(self,gameState: GameState,a,b,count):
        agentNum = count % gameState.getNumAgents()

        if gameState.isWin() or gameState.isLose() or count >= self.depth * gameState.getNumAgents():
            return None, self.evaluationFunction(gameState)
        elif agentNum == 0:
            return self.maxVal(gameState,a,b,count)
        else:
            return self.minVal(gameState,a,b,count)

    def maxVal(self,gameState: GameState,a,b,count):
        agentNum = count % gameState.getNumAgents()

        if gameState.isWin() or gameState.isLose() or count >= self.depth * gameState.getNumAgents():
            return None, self.evaluationFunction(gameState)
        
        bestAct, v = None, -math.inf
        for act in gameState.getLegalActions(agentNum):
            u = self.abPruning(gameState.generateSuccessor(agentNum,act),a,b,count+1)[1]
            if u > v:
                bestAct, v = act, u
            if v > b: # Pruning
                return None, v
            a = max(a,v)
        return bestAct, v
    
    def minVal(self,gameState: GameState,a,b, count):
        agentNum = count % gameState.getNumAgents()

        if gameState.isWin() or gameState.isLose() or count >= self.depth * gameState.getNumAgents():
            return None, self.evaluationFunction(gameState)
        
        bestAct, v = None, math.inf
        for act in gameState.getLegalActions(agentNum):
            u = self.abPruning(gameState.generateSuccessor(agentNum,act),a,b,count+1)[1]
            if u < v:
                bestAct, v = act, u
            if v < a: # Pruning
                return None, v
            b = min(b,v)
        return bestAct, v

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        return self.expectiMax(gameState,0)[0]
    
    def expectiMax(self,gameState: GameState,count):
        agentNum = count % gameState.getNumAgents()

        if gameState.isWin() or gameState.isLose() or count >= self.depth * gameState.getNumAgents():
            return None,self.evaluationFunction(gameState)
        
        if agentNum == 0:
            x, maxAction = -math.inf, None
            for action in gameState.getLegalActions(agentNum):
                util = self.expectiMax(gameState.generateSuccessor(agentNum,action),count+1)[1]
                if util > x:
                    maxAction, x = action, util
            return maxAction, x
        else: # The ghosts do not act optimally, but randomly
            actions = gameState.getLegalActions(agentNum)
            numActions = len(actions)
            act = actions[random.randint(0,numActions-1)]
            utils = [self.expectiMax(gameState.generateSuccessor(agentNum,a),count+1)[1] for a in actions]
            
            return act, np.mean(utils) 

def betterEvaluationFunction(currentGameState: GameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    ghostStates = currentGameState.getGhostStates()
    scaredTimes = [ghostState.scaredTimer for ghostState in ghostStates]

    food = currentGameState.getFood()
    numFood = currentGameState.getNumFood()

    capsules = currentGameState.getCapsules()
    numCaps = len(capsules)

    pos = currentGameState.getPacmanPosition()

    # Get the distance to the closest ghost
    ghostPositions = [x.getPosition() for x in ghostStates]
    closestGhostDistance = min([euclideanDist(x,pos) for x in ghostPositions])
    
    # Get distance to closest food
    closestFoodDistance = min([manhattanDist(i,pos) for i, x in np.ndenumerate(list(food)) if x]) if numFood else math.inf

    # Get distance to closest capsule
    closestCapsuleDistance = min([manhattanDist(i,pos) for i in capsules]) if numCaps else math.inf

    if currentGameState.isLose():
        return -math.inf
    if currentGameState.isWin():
        return math.inf

    score = (20/closestFoodDistance) + (15/closestGhostDistance) + (10/closestCapsuleDistance) + sum(scaredTimes) + currentGameState.getScore()
    return score
    

# Abbreviation
better = betterEvaluationFunction
