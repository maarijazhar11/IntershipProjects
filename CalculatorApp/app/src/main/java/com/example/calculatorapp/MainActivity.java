package com.example.calculatorapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private EditText resultEditText;
    private StringBuilder input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultEditText = findViewById(R.id.result);
        input = new StringBuilder();
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        input.append(button.getText().toString());
        resultEditText.setText(input.toString());
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        String currentOperator = button.getText().toString();
        input.append(" ").append(currentOperator).append(" ");
        resultEditText.setText(input.toString());
    }

    public void onClearClick(View view) {
        input.setLength(0);
        resultEditText.setText("");
    }

    @SuppressLint("SetTextI18n")
    public void onEqualsClick(View view) {
        try {
            double result = evaluateExpression(input.toString());
            resultEditText.setText(String.valueOf(result));
            input.setLength(0);
            input.append(result);
        } catch (Exception e) {
            resultEditText.setText("Error");
        }
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                numbers.push(Double.parseDouble(token));
            } else if (token.length() == 1 && "+-*/".contains(token)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token.charAt(0))) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(token.charAt(0));
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            default: throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            default: return -1;
        }
    }
}
