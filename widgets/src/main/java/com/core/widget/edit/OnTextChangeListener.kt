package com.tsy.widget.edit

import android.text.Editable

/**
 *
 * @author like
 * @date 8/21/21 1:51 PM
 */
interface OnTextChangeListener {

    fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){

    }

    fun afterTextChanged(s: Editable?){

    }

    fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int)

}