package br.com.caelum.ondeestaobusao.generic.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class GenericFragmentManager {
	private List<ViewState> stack = new ArrayList<ViewState>();
	private final Activity activity;

	public GenericFragmentManager(Activity activity) {
		this.activity = activity;
	}
	
	public GenericFragmentManager replace(int fragmentId, GenericFragment fragment) {
		return this.replace(fragmentId, fragment, false);
	}
	
	public GenericFragmentManager replace(int fragmentId, GenericFragment fragment, boolean addToBackStack) {
		ViewGroup parent  = (ViewGroup) activity.findViewById(fragmentId);

		if (addToBackStack) {
			stack.add(0, new ViewState(parent));
		}
		
		parent.removeAllViews();
		
		View view = fragment.onCreateView(activity, parent);		
		parent.addView(view);
		
		return this;
	}

	public boolean back() {
		if (!stack.isEmpty()) {
			ViewState state = stack.remove(0);
			state.restoreLast();
			return true;
		}
		
		return false;
	}
	
	private class ViewState {
		private ViewGroup parent;
		private List<View> currentChildren;
		
		public ViewState(ViewGroup parent) {
			this.parent = parent;
			extractChildren(this.parent);
		}
		
		private void extractChildren(ViewGroup group) {
			for (int i = 0; i < group.getChildCount(); i++) {
				currentChildren.add(group.getChildAt(i));
			}
		}

		public void restoreLast() {
			parent.removeAllViews();
			for (View view : currentChildren) {
				parent.addView(view);
			}
		}
	}
	

}
