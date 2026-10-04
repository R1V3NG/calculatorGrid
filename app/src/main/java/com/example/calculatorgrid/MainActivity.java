package com.example.calculatorgrid;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editFirst, editSecond;
    private TextView textOp, textResult;
    private Button btnSum, btnDif, btnMult, btnDiv, btnClear;

    private String currentOp = "+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editFirst   = findViewById(R.id.editTextFirst);
        editSecond  = findViewById(R.id.editTextSecond);
        textOp      = findViewById(R.id.textOp);
        textResult  = findViewById(R.id.textResult);

        btnSum   = findViewById(R.id.buttonSum);
        btnDif   = findViewById(R.id.buttonDif);
        btnMult  = findViewById(R.id.buttonMult);
        btnDiv   = findViewById(R.id.buttonDiv);
        btnClear = findViewById(R.id.buttonClear);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { calculate(); }
        };
        editFirst.addTextChangedListener(watcher);
        editSecond.addTextChangedListener(watcher);

        // Выбор операции — меняем символ между операндами и пересчитываем
        btnSum.setOnClickListener(v  -> setOp("+"));
        btnDif.setOnClickListener(v  -> setOp("-"));
        btnMult.setOnClickListener(v -> setOp("*"));
        btnDiv.setOnClickListener(v  -> setOp("/"));

        // Кнопка "C" — очистка всего
        btnClear.setOnClickListener(v -> {
            editFirst.setText("");
            editSecond.setText("");
            textResult.setText("");
            currentOp = "+";
            textOp.setText("+");
        });
    }

    private void setOp(String op) {
        currentOp = op;
        textOp.setText(op);
        calculate();
    }

    private void calculate() {
        String a = editFirst.getText().toString().trim();
        String b = editSecond.getText().toString().trim();

        // Если поле пустое или введена только точка/минус — результата нет
        if (a.isEmpty() || b.isEmpty()
                || a.equals(".") || b.equals(".")
                || a.equals("-") || b.equals("-")) {
            textResult.setText("");
            return;
        }

        try {
            double x = Double.parseDouble(a);
            double y = Double.parseDouble(b);
            double res;

            switch (currentOp) {
                case "+":
                    res = x + y;
                    break;
                case "-":
                    res = x - y;
                    break;
                case "*":
                    res = x * y;
                    break;
                case "/":
                    if (y == 0) {
                        // Бросаем исключение и ловим его ниже
                        throw new ArithmeticException("Division by zero");
                    }
                    res = x / y;
                    break;
                default:
                    res = 0;
            }

            textResult.setText(format(res));

        } catch (ArithmeticException e) {
            // Деление на ноль и прочие арифметические ошибки
            textResult.setText("Ошибка");
        } catch (NumberFormatException e) {
            // На всякий случай — если пользователь ввёл что-то некорректное
            textResult.setText("");
        }
    }

    private String format(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) {
            return "Error";
        }
        if (v == Math.floor(v) && Math.abs(v) < 1e15) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}