/*
@author Christian Baun
for assignment 1 Programming Language Design 2018, DIKU.KU.DK
This program was created with inspiration from
https://www.geeksforgeeks.org/stack-set-2-infix-to-postfix/
I would say the switch and the operator case in the algorithm
not my design.
*/

import java.util.Scanner;
import java.util.Stack;

public class FromInfixToPostfixAndPrefix {

    // regular expressions
    static String alphaNum = "(\\w)";         // single word character
    static String operator = "([\\*|/|+|-])"; // single operator

    // main method
    public static void main(String[] args) {

        // get input
        System.out.println("Write your expression:");
        Scanner input = new Scanner(System.in);
        String expr = input.nextLine();

        // trim
        expr = expr.replaceAll("\\s+", "");

        // call methods and output result
        System.out.println(
                          "Input: "   + expr +
                        "\nPrefix: "  + toPrefix(expr) +
                        "\nPostfix: " + toPostfix(expr));
    }

    // method for precedence
    public static int precedence(char c){
        switch (c){
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return 0;
    }

    // method to swap all parenthesis
    private static StringBuilder swapParenthesis(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == '(') {
                c = ')';
            } else if (c == ')') {
                c = '(';
            }
            sb.append(c);
        }
        return sb;
    }

    // infix to postfix method
    private static String toPostfix(String expr){

        Stack<Character> stackOperators = new Stack<>();
        // using stringbuilder for faster string operations
        StringBuilder out = new StringBuilder();

        // check all chars in expression
        for (int i = 0; i<expr.length();i++) {

            // get char and string
            char ch = expr.charAt(i);
            String cs = Character.toString(ch);

            // case of alpha numeric
            if (cs.matches(alphaNum)){
                out.append(cs);
            }
            // case of start parenthesis, then make recursive
            // method call on this block content
            else if (ch == '(') {
                // get loc of end parenthesis
                int end = expr.lastIndexOf(')');
                out.append("(");
                out.append(toPostfix(expr.substring(i+1,end)));
                out.append(")");
                // jump for-loop till end of parenthesis block
                i = end;
            }
            // case of operator
            else if (cs.matches(operator)){
                while (!stackOperators.isEmpty() && precedence(ch)
                        <= precedence(stackOperators.peek())) {
                    out.append(stackOperators.pop());
                }
                stackOperators.push(ch);
            }
        }
        //append last operators
        while (!stackOperators.isEmpty()) {
            out.append(stackOperators.pop());
        }
        //return result of stringbuilder as String
        return String.valueOf(out);
    }

    // infix to prefix method using the infix to postfix method
    private static String toPrefix(String expr){

        // swap parenthesis, so toPostfix() works
        StringBuilder sb = swapParenthesis(expr);
        // reverse string
        String result = sb.reverse().toString();
        // get postfix
        result = toPostfix(result);
        // swap parenthesis back
        sb = swapParenthesis(result);
        // reverse back
        result = sb.reverse().toString();

        return result;
    }
}

