package com.keo.onsite.linkalinpay.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.keo.onsite.linkalinpay.R;

public class Recyclerviewmargin extends RecyclerView.ItemDecoration {

private final int verticalspacingheight;


public Recyclerviewmargin(int verticalspacingheight){
    this.verticalspacingheight=verticalspacingheight;

}

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);
    outRect.bottom=verticalspacingheight;

}
}





