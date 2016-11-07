package top.titov.mathforkids;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{

    private static final int MAX_NUMBER = 5;
    private static final int ANSWER_COUNT = 3;

    public List<String> itemList = new ArrayList<>();
    private GridView answerGridView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> numberList = new ArrayList<>();

    private Integer firstNum;
    private Integer secondNum;
    private Integer result;

    private TextView firstNumTextView;
    private TextView secondNumTextView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNumTextView = (TextView) findViewById(R.id.firstTextView);
        secondNumTextView = (TextView) findViewById(R.id.secondTextView);
        answerGridView = (GridView) findViewById(R.id.answerGrid);

        handler = new Handler(this.getMainLooper());

        generateEquation();
        clearAndFillAnswerList();

        adapter = new ArrayAdapter<>(this, R.layout.answer_item, itemList);
        answerGridView.setAdapter(adapter);
        answerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                (MainActivity.this).onItemClick(view, i);
            }
        });
    }

    private void generateEquation() {
        Random random = new Random(System.currentTimeMillis());
        firstNum = random.nextInt(MAX_NUMBER);
        secondNum = random.nextInt(MAX_NUMBER);
        firstNumTextView.setText(String.valueOf(firstNum));
        secondNumTextView.setText(String.valueOf(secondNum));
        result = firstNum + secondNum;
    }

    private void clearAndFillAnswerList() {
        itemList.clear();
        boolean hasAnswer = false;
        fillAnswerArray();
        for (int i = 0; i < ANSWER_COUNT; i++) {
            Integer answer = getNextAnswer();
            if (answer.equals(result)) hasAnswer = true;
            itemList.add(String.valueOf(getNextAnswer()));
        }
        if (!hasAnswer){
            Random random = new Random(System.currentTimeMillis());
            itemList.set(random.nextInt(ANSWER_COUNT), String.valueOf(result));
        }

    }

    private void fillAnswerArray() {
        for (int i = 0; i < MAX_NUMBER * 2 + 1; i++) {
            numberList.add(i);
        }
    }

    private Integer getNextAnswer() {
        Random random = new Random(System.currentTimeMillis());
        int position = random.nextInt(numberList.size());
        Integer answer = numberList.get(position);
        numberList.remove(position);
        numberList.trimToSize();
        return answer;
    }

    public void onItemClick(View view, int position) {
        Integer answer = Integer.parseInt(itemList.get(position));
        if (answer.equals(result)) {
            view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateEquation();
                    clearAndFillAnswerList();
                    adapter = new ArrayAdapter<>(MainActivity.this, R.layout.answer_item, itemList);
                    answerGridView.setAdapter(adapter);
                }
            }, 1000);
        } else
            view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));
    }
}
