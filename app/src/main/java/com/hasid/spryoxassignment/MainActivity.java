package com.hasid.spryoxassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    XmlPullParserFactory pullParserFactory;

    ArrayList<String> attributeNames = new ArrayList<>();
    ArrayList<String> attributeTime = new ArrayList<>();
    ArrayList<String> nbTest = new ArrayList<>();
    Entry e = new Entry();
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attributeNames.clear();
        XMLParser();
        chart = findViewById(R.id.linechart);
        chart();
    }

//XML data parsing using XMLPullParser
    private void XMLParser() {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getAssets().open("runtests.output.all.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
             parseXML(parser);
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private ArrayList<Component> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Component> countries = null;
        int eventType = parser.getEventType();
        Component country = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    countries = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("Component")) {
                        nbTest.add(parser.getAttributeValue(3));
                        attributeNames.add(parser.getAttributeValue(0));
                        attributeTime.add(parser.getAttributeValue(2));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();

            }
            eventType = parser.next();
        }

        return countries;

    }

    private void chart() {
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setNoDataText(null);
        chart.invalidate();
        chart.setDescription(null);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawAxisLine(false);

        //Set YAxis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);

        //Create and Initialize ArrayList
        List<Entry> component = new ArrayList<>();
        List<Entry> time = new ArrayList<>();

        //Add data in ArrayList
        for (int i = 0; i < attributeNames.size(); i++) {
            component.add(new Entry(i, Float.parseFloat(nbTest.get(i))));
            time.add(new Entry(i, Float.parseFloat(attributeTime.get(i))));
        }
        LineDataSet dataset = new LineDataSet(component, "Component");
        dataset.setColor(Color.GRAY);
        LineDataSet datasetTime = new LineDataSet(time, "Time");
        datasetTime.setColor(Color.GRAY);

        //Set Data in Graph
        LineData lineData = new LineData(dataset, datasetTime);
        chart.setData(lineData);
        chart.invalidate();
    }
}
