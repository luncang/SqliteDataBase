package com.stay4it;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.stay4it.db.DBManager1;
import com.stay4it.db.utilities.Trace;
import com.stay4it.model.Company;
import com.stay4it.model.Developer;
import com.stay4it.model.Skill;

import java.util.ArrayList;

/**
 * Created by Stay on 12/6/16.
 * Powered by www.stay4it.com
 */
public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mDbAddBtn).setOnClickListener(this);
        findViewById(R.id.mDbDeleteBtn).setOnClickListener(this);
        findViewById(R.id.mDbQueryBtn).setOnClickListener(this);
        DBManager1.init(getApplicationContext(), new DatabaseHelper(getApplicationContext()));
    }


    public void add() {
        Company company = new Company();
        company.setId("00001");
        company.setName("stay4it");
        company.setUrl("www.stay4it.com");
        company.setTel("10086");
        company.setAddress("Shanghai");
        Developer developer = new Developer();
        developer.setId("00001");
        developer.setName("Stay");
        developer.setAge(17);
        Skill skill = new Skill();
        skill.setName("coding");
        skill.setDesc("android");
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(skill);
        developer.setSkills(skills);
        ArrayList<Developer> developers = new ArrayList<Developer>();
        developers.add(developer);
        company.setDevelopers(developers);
        DBManager1.getInstance().newOrUpdate(company);
    }

    public void queryCompanyById() {
        Company company = DBManager1.getInstance().queryById(Company.class, "00001");
        if (company != null) {
            Trace.d(company.toString());
        }
    }

    public void deleteCompanyById() {
        Company company = new Company();
        company.setId("00001");
        DBManager1.getInstance().delete(company);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mDbAddBtn:
                add();
                break;
            case R.id.mDbQueryBtn:
                queryCompanyById();
                break;
            case R.id.mDbDeleteBtn:
                deleteCompanyById();
                break;
        }
    }
}
