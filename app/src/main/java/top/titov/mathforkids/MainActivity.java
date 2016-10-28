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

public class MainActivity extends AppCompatActivity {

    private static final int MAX_NUMBER = 5;
    private static final int ANSWER_COUNT = 3;

    public List<String> itemList = new ArrayList<>();
    private GridView answerGridView;
    private ArrayAdapter<String> adapter;
    private List<Integer> numberList = new ArrayList<>();
    private Integer firstNum;
    private Integer secondNum;
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
        fillNumbers();
        clearAndFillItemList();
        adapter = new ArrayAdapter<>(this, R.layout.answer_item, itemList);
        answerGridView.setAdapter(adapter);
        answerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                Integer result = Integer.parseInt(itemList.get(i));
                if (result.equals(firstNum+secondNum)) {
                    view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fillNumbers();
                            clearAndFillItemList();
                            adapter.notifyDataSetChanged();
                        }
                    }, 3000);
                } else view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));

            }
        });
    }

    private void fillNumbers() {
        Random random = new Random(System.currentTimeMillis());
        firstNum = random.nextInt(MAX_NUMBER);
        secondNum = random.nextInt(MAX_NUMBER);
        firstNumTextView.setText(String.valueOf(firstNum));
        secondNumTextView.setText(String.valueOf(secondNum));

    }

    private void clearAndFillItemList() {
        itemList.clear();
        fillAnswerArray();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < ANSWER_COUNT; i++) {
            itemList.add(String.valueOf(getNextAnswer(random)));
        }
        String answer = String.valueOf(firstNum + secondNum);
        itemList.set(random.nextInt(ANSWER_COUNT), answer);
    }

    private void fillAnswerArray() {
        for (int i = 0; i < MAX_NUMBER * 2 + 1; i++) {
            numberList.add(i);
        }
    }

    private Integer getNextAnswer(Random random) {
        int position = random.nextInt(numberList.size());
        Integer answer = numberList.get(position);
        numberList.remove(position);
        return answer;
    }
}
