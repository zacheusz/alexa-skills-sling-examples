/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>
 */
package eu.zacheusz.alexa.example.sdk_speechlet_overrides;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import org.apache.felix.scr.annotations.*;

import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * @author zacheusz
 */
//@Component
//@Service(SpeechletV2.class) //TODO uncomment annotations to override AlexaSlingSpeechlet and use it instead of handlers
public class ExampleSpeechletOverrides implements SpeechletV2 {

    private static final String SLOT_NAME = "channel";

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) {

    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        return newTellResponse("Hello!");
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {

        final Intent intent = speechletRequestEnvelope.getRequest().getIntent();
        final Slot slot = intent.getSlot(SLOT_NAME);
        final String intentName = intent.getName();

        final String responseMessage;
        if (slot == null) {
            responseMessage = format(
                    "I got your request for intent %s, but there is no slot %s.",
                    intentName, SLOT_NAME);
        } else {
            final String value = slot.getValue();
            responseMessage = format(
                    "I got your request for intent %s. Slot value is %s. Thanks!",
                    intentName, value);
        }

        return newTellResponse(responseMessage);
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {

    }

    private SpeechletResponse newTellResponse(final String text) {
        final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);
        return SpeechletResponse.newTellResponse(speech);
    }

}
