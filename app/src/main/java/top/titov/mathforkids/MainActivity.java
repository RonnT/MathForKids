package top.titov.mathforkids;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_NUMBER = 5;
    private static final int ANSWER_COUNT = 2;

    public List<String> variantList = new ArrayList<>();
    private GridView answerGridView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> numberLineList = new ArrayList<>();

    private Integer firstNum;
    private Integer secondNum;
    private Integer result;

    private TextView firstNumTextView;
    private TextView secondNumTextView;
    private Handler handler;

    private ViewGroup resultLayout;
    private TextView totalText;
    private TextView rightText;
    private TextView wrongText;

    private int total;
    private int right;
    private int wrong;

    private boolean isChallenge = true;

    private AdapterView.OnItemClickListener onGridClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            (MainActivity.this).onItemClick(view, i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNumTextView = (TextView) findViewById(R.id.firstTextView);
        secondNumTextView = (TextView) findViewById(R.id.secondTextView);
        answerGridView = (GridView) findViewById(R.id.answerGrid);

        resultLayout = findViewById(R.id.resultLayout);
        totalText = findViewById(R.id.totalText);
        rightText = findViewById(R.id.rightText);
        wrongText = findViewById(R.id.wrongText);

        handler = new Handler(this.getMainLooper());

        generateEquation();
        clearAndFillAnswerList();

        adapter = new ArrayAdapter<>(this, R.layout.answer_item, variantList);
        answerGridView.setAdapter(adapter);
        answerGridView.setNumColumns(ANSWER_COUNT);
    }

    private void generateEquation() {
        Random random = new Random(System.currentTimeMillis());
        firstNum = random.nextInt(MAX_NUMBER + 1);
        secondNum = random.nextInt(MAX_NUMBER + 1);
        firstNumTextView.setText(String.valueOf(firstNum));
        secondNumTextView.setText(String.valueOf(secondNum));
        result = firstNum + secondNum;
    }

    private void clearAndFillAnswerList() {
        totalText.setText(getString(R.string.total, total));
        rightText.setText(getString(R.string.right, right));
        wrongText.setText(getString(R.string.wrong, wrong));
        answerGridView.setOnItemClickListener(onGridClickListener);
        variantList.clear();
        boolean hasAnswer = false;
        fillNumberLine();
        for (int i = 0; i < ANSWER_COUNT; i++) {
            Integer answer = getNextAnswer();
            if (answer.equals(result)) hasAnswer = true;
            variantList.add(String.valueOf(answer));
        }
        if (!hasAnswer) {
            Random random = new Random(System.currentTimeMillis());
            variantList.set(random.nextInt(ANSWER_COUNT), String.valueOf(result));
        }
    }

    private void fillNumberLine() {
        numberLineList.clear();
        for (int i = 0; i < MAX_NUMBER * 2 + 1; i++) {
            numberLineList.add(i);
        }
    }

    private Integer getNextAnswer() {
        Random random = new Random(System.currentTimeMillis());
        int position = random.nextInt(numberLineList.size());
        Integer answer = numberLineList.get(position);
        numberLineList.remove(position);
        numberLineList.trimToSize();
        return answer;
    }

    public void onItemClick(View view, int position) {
        Integer answer = Integer.parseInt(variantList.get(position));
        if (answer.equals(result)) {
            onRightAnswer(view);
        } else onWrongAnswer(view);

    }

    private void onRightAnswer(View clickedView){
        clickedView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
        total++;
        right++;
        if (isChallenge) answerGridView.setOnItemClickListener(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateEquation();
                clearAndFillAnswerList();
                adapter = new ArrayAdapter<>(MainActivity.this, R.layout.answer_item, variantList);
                answerGridView.setAdapter(adapter);
            }
        }, 500);
    }

    private void onWrongAnswer(View clickedView){
        clickedView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));
        total++;
        wrong++;
        if (isChallenge) answerGridView.setOnItemClickListener(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateEquation();
                clearAndFillAnswerList();
                adapter = new ArrayAdapter<>(MainActivity.this, R.layout.answer_item, variantList);
                answerGridView.setAdapter(adapter);
            }
        }, 500);
    }
}
