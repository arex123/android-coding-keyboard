package com.example.codekeyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;



public class CustomKeyboardApp extends InputMethodService
    implements KeyboardView.OnKeyboardActionListener {

    private boolean isCapsLockOn = false;

    KeyboardView keyboardView;


    @Override
    public View onCreateInputView() {

         keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.custom_keyboard_layout,null);
        Keyboard keyboard = new Keyboard(this,R.xml.customkeypad);


        keyboardView.setKeyboard(keyboard);


        keyboardView.setOnKeyboardActionListener(this);

        keyboardView.setPreviewEnabled(false);


        return keyboardView;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {


    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        InputConnection inputConnection = getCurrentInputConnection();

        if(inputConnection==null)
            return ;

        if(primaryCode==-5){
            //for deleting last character
//            inputConnection.deleteSurroundingText(1,0);

            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        }else if(primaryCode==15){
            inputConnection.deleteSurroundingText(0, 1);

        }else if (primaryCode == -6) {

            EditorInfo editorInfo = getCurrentInputEditorInfo();
            // Check if the input field is multi-line or not
            if ((editorInfo.inputType & InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0) {
                // If it's a multi-line field, insert a new line
                inputConnection.commitText("\n", 1);
            } else {
                // Otherwise, handle it like a form submission or "Enter" action
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_SEARCH);
            }

            //for moving to newline
//            inputConnection.commitText("\n", 1);
        }else if (primaryCode == 32) {  // Key code for space is 32


                // Handle normal space key press
                inputConnection.commitText(" ", 1);


        }else if(primaryCode == 10){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showInputMethodPicker(); // Show the system input method picker
            }
        }else if(primaryCode==13){
            //caplock
            isCapsLockOn = !isCapsLockOn;
            updateKeyboard();

        }else{
            char keyChar = (char) primaryCode;

            // Apply Caps Lock state if the character is a letter
            if (Character.isLetter(keyChar)) {
                keyChar = isCapsLockOn ? Character.toUpperCase(keyChar) : Character.toLowerCase(keyChar);
            }

            // Commit the text with the adjusted character
            inputConnection.commitText(String.valueOf(keyChar), 1);
        }



    }

    private void updateKeyboard() {
        if (keyboardView != null) {
            Keyboard keyboard = keyboardView.getKeyboard();
            if (keyboard != null) {
                for (Keyboard.Key key : keyboard.getKeys()) {
                    if (key.label != null && key.label.length() == 1 && Character.isLetter(key.label.charAt(0))) {
                        String label = key.label.toString(); // Convert CharSequence to String
                        key.label = isCapsLockOn ? label.toUpperCase() : label.toLowerCase();
                    }
                }
                keyboardView.invalidateAllKeys();
            }
        }
    }


    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
