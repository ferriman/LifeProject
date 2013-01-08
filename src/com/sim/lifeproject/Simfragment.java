package com.sim.lifeproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



@SuppressLint("HandlerLeak")
public class Simfragment extends Fragment {
	
	View view = null;
	TextView info_sim;
	View pantalla;
	Thread bucleprincipal;
	static Handler gestor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		if (Engine.ISTABLET==true) {
			view = inflater.inflate(R.layout.simtablet, null);
		} else {
			view = inflater.inflate(R.layout.sim, null);
		}
		info_sim=(TextView) view.findViewById(R.id.show_info);
		pantalla = new Simulator(getActivity().getApplicationContext());
		pantalla.requestFocus();
		LinearLayout canvassim = (LinearLayout) view.findViewById(R.id.pantallasimulacio);
        canvassim.addView(pantalla);
        gestor = new Handler() {
            public void handleMessage(Message msg) {
            	switch (msg.what) {
            	case LifeProjectActivity.UPDATEID:
            		if (Engine.PLAY==true) { Engine.evolve(); Engine.buildgraph(); pantalla.invalidate(); }
            		float specie1DeadValue;
            		float specie2DeadValue;
            		if (Engine.SPECIE1_TOTAL_UNITS!=0) {
            			specie1DeadValue=(float)Math.round(((float)Engine.SPECIE1_LAST_DEADS*100/(float)Engine.SPECIE1_TOTAL_UNITS)*100)/100;
            		} else {
            			specie1DeadValue=0;
            		}
            		if (Engine.SPECIE2_TOTAL_UNITS!=0) {
            			specie2DeadValue=(float)Math.round(((float)Engine.SPECIE2_LAST_DEADS*100/(float)Engine.SPECIE2_TOTAL_UNITS)*100)/100;
            		} else {
            			specie2DeadValue=0;
            		}
            		info_sim.setText("Plants: "+(float)Engine.PLANTS_TOTAL_UNITS/100+"% ("+Engine.LOOPS/365+" years and "+Engine.LOOPS%365+" days)\nRed specie: "+Engine.SPECIE1_TOTAL_UNITS+" alive (death ratio: "+(float)specie1DeadValue+"%)\nBlue specie: "+Engine.SPECIE2_TOTAL_UNITS+" alive (death ratio: "+(float)specie2DeadValue+"%)");
        			
            		break;
            	}
            	super.handleMessage(msg);
            }
        };
        bucleprincipal = new Thread() {
        	public void run() {
        		while (true) {
        			Message msg = new Message();
        			msg.what= LifeProjectActivity.UPDATEID;
       				Simfragment.gestor.sendMessage(msg);
      					try {
      						Thread.sleep(400);
      					} catch (InterruptedException e) {
      						Engine.PLAY=false;
        			}
        		}
        	}
        };
        bucleprincipal.start();
		
		return view; 
	}
	
	

}