package aitest;

public enum AiParameter {
	Port,		// the ai will value hexes with ports on them
	Random,		// the ai will play randomly, this parameter overwrites all other parameters
	NewRes,		// the ai will value territories with new resource or new numbers more
	PrintStatus,// the ai prints its status to the console, this feature will be replaced with a much cooler one
	Lumbrick,	// materials' base values will initialize to value lumber and brick the most. See ai/material for details
	Orain;		// materials' base values will initialize to value ore and grain the most. See ai/material for details
}
