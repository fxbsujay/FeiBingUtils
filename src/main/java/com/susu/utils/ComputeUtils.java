package com.susu.utils;


import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: Four arithmetic</p>
 * <p>四则运算</p>
 * @author sujay
 * @version 23:48 2022/2/10
 * @see java.util.Date
 * @since JDK1.8
 */
public class ComputeUtils {

    private static final Pattern PATTERN = Pattern.compile("(?<!\\d)-?\\d+(\\.\\d+)?|[+\\-*/()]");

    /**
     * <p>Description: Four arithmetic</p>
     * <p>四则运算</p>
     * @param formula 公式
     */
    public static String compute(String formula) throws Exception {
        System.out.println("计算"+formula);
        /*数字栈*/
        Stack<Double> number = new Stack<Double>();
        /*符号栈*/
        Stack<String> operator = new Stack<String>();
        operator.push(null);

        /* 将expr打散为运算数和运算符 */
        Matcher m = PATTERN.matcher(formula);
        while(m.find()) {
            String temp = m.group();
            if(temp.matches("[+\\-*/()]")) {
                // 遇到符号
                if("(".equals(temp)) {
                    operator.push(temp);
                }else if(")".equals(temp)){
                    // 遇到右括号，"符号栈弹栈取栈顶符号b，数字栈弹栈取栈顶数字a1，数字栈弹栈取栈顶数字a2，计算a2 b a1 ,将结果压入数字栈"，重复引号步骤至取栈顶为左括号，将左括号弹出
                    String b = null;
                    while(!"(".equals(b = operator.pop())) {
                        System.out.println("符号栈更新："+operator);
                        double a1 = number.pop();
                        double a2 = number.pop();
                        number.push(doubleCal(a2, a1, b.charAt(0)));
                    }
                }else {
                    // 遇到运算符，满足该运算符的优先级大于栈顶元素的优先级压栈；否则计算后压栈
                    while(getPriority(temp) <= getPriority(operator.peek())) {
                        double a1 = number.pop();
                        double a2 = number.pop();
                        String b = operator.pop();
                        number.push(doubleCal(a2, a1, b.charAt(0)));
                    }
                    operator.push(temp);
                }
            }else {
                //遇到数字，直接压入数字栈
                number.push(Double.valueOf(temp));
            }
        }

        // 遍历结束后，符号栈数字栈依次弹栈计算，并将结果压入数字栈
        while(operator.peek()!=null) {
            double a1 = number.pop();
            double a2 = number.pop();
            String b = operator.pop();
            number.push(doubleCal(a2, a1, b.charAt(0)));
        }
        return number.pop()+"";
    }

    /**
     * <p>Description: Simple calculation</p>
     * <p>简单计算</p>
     * @param param1 参数
     * @param param2 参数
     * @param operator 符号
     */
    private static double doubleCal(double param1, double param2, char operator) throws Exception {
        switch (operator) {
            case '+':
                return param1 + param2;
            case '-':
                return param1 - param2;
            case '*':
                return param1 * param2;
            case '/':
                return param1 / param2;
            default:
                break;
        }
        throw new Exception("illegal operator!");
    }

    /**
     *  <p>Description: Get priority</p>
     *  <p>获取优先级</p>
     * @param s 符号
     */
    private static int getPriority(String s) throws Exception {
        if(s == null) {
            return 0;
        }
        switch(s) {
            case "(":
                return 1;
            case "+":;
            case "-":
                return 2;
            case "*":;
            case "/":
                return 3;
            default:
                break;
        }
        throw new Exception("illegal operator!");
    }

    public static void main(String[] args) throws Exception {
        String str = "-3.5*(4.5-(4+(-1-1/2)))";
        System.out.println(compute(str));
    }
}
