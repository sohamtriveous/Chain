package com.algorithm.chain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Performs all the chain operations, synchronous at the current moment (so needs to be invoked from another thread)
 */
public class Chain {
    // the map of a key (the first character of a string) to the list of all the strings with that key as the first character
    private static Map<String, List<String>> firstMap;
    // this is the final list of all possible combinations of the chain
    private static List<List<String>> masterList;
    // a list of the initial strings split by the delimiter
    private static List<String> splitStrings;

    /**
     * Creates a forward lookup or a dictionary based on the first letter of a string
     * It first splits the strings assuming whitespace as a delimiter
     * For example a: alpha america angel b: beta belgium etc
     * @param string the overall
     */
    public static void createLookup(String string) {
        if (string != null) {
            // initialize all the lists
            if (masterList != null) {
                masterList = null;
            }
            masterList = new ArrayList<List<String>>();
            if (firstMap != null) {
                firstMap = null;
            }
            firstMap = new HashMap<String, List<String>>();
            // split the strings
            splitStrings = split(string);
            // add the springs to the map
            for (String splitString : splitStrings) {
                addToFirst(splitString);
            }
        }
    }

    /**
     * Splits a string using a whitespace delimiter
     * Note: I didn't have time to check of duplicate entries here, can be done with more time :)
     * @param string a list of strings
     * @return
     */
    public static List<String> split(String string) {
        if (string != null) {
            return Arrays.asList(string.split("\\s+"));
        }
        return null;
    }

    /**
     * Adds a given string to the hashmap/dictionary, also checks for duplicates
     * @param string
     */
    private static void addToFirst(String string) {
        if (string != null && firstMap != null) {
            String key = string.substring(0, 1);
            List<String> list = firstMap.get(key);
            if (list == null) {
                list = new ArrayList<String>();
            }
            // make sure the string does not already exist in the map
            if (!list.contains(string)) {
                list.add(string);
                firstMap.put(key, list);
            }
        }
    }

    /**
     * This traverses the list of strings and returns a string result that can be consumed by the user
     * @return a human consumable result (that is displayed in the textview)
     */
    public static String traverse() {
        // initialise some strings that represents aspects of the results
        String result = "";
        String possibilities = "";
        // find all the keys and traverse the paths for each entry for that key
        for (String key : firstMap.keySet()) {
            List<String> nodes = firstMap.get(key);
            for (String node : nodes) {
                // this path represents the path of nodes, will eventually contain all the paths for a node in a graph
                List<String> path = new ArrayList<String>();
                // add the first entry to the path
                path.add(node);
                // begin the recursive traversal, more details in the method itself
                traverse(path);
            }
        }
        // we're done with the data collection, time to understand the results and find out if there is a "solution"
        int max = 0, position = 0;
        // find the path with the maximum length, note down the position
        for (int i = 0; i < masterList.size(); i++) {
            List<String> path = masterList.get(i);
            if (path.size() > max) {
                max = path.size();
                position = i;
            }
            // add it to a "possibilities" string so that we can display it later
            possibilities += (i + 1) + ". " + path.toString() + "\n";
        }
        // check if the solution criteria is satisfied and print the relevant message
        if (max == splitStrings.size()) {
            result += "Success, solution exists for size " + max + " at position " + (position + 1) + " and the result is " + masterList.get(position).toString();
        } else {
            result += "No solution. The nearest solution has a size of " + max + " , it is at position " + (position + 1) + " and the result is " + masterList.get(position).toString();
        }
        return result + "\n\n" + possibilities;
    }

    /**
     * The heart of the whole application: a recursive method that traverses the graph and adds it to the master
     * list of possible paths under certain conditions
     * @param path The existing list of path for this node (the path of the node till now ie. all the nodes that have come before this node in this path)
     *                example for if we are at goa in a path of book king goa, when entering, it will be book king
     */
    private static void traverse(List<String> path) {
        // find the last node in the path till now (for a path of "book king", this would be "king")
        String lastNode = path.get(path.size() - 1);
        // find the key for the next lookup, for "king" this would be "g"
        String keyForLastNode = lastNode.substring(lastNode.length() - 1);
        // find the entries for that key "g": {goa, game, goalkeeper}
        List<String> nextNodes = firstMap.get(keyForLastNode);
        // add the current path to the list of all paths
        masterList.add(path);
        // if there are no entries for key "g" return (do nothing)
        if (nextNodes == null) {
            return;
        }
        // if there are entries for key "g", call this method recursively for each entry
        // ie. call recursively for "goa", "game", "goalkeeper"
        for (String nextNode : nextNodes) {
            if (!path.contains(nextNode)) {
                // create a copy of the path till now
                List<String> newStringList = new ArrayList<String>(path);
                newStringList.add(nextNode);
                // make a recursive traversal
                traverse(newStringList);
            }
        }
    }
}
