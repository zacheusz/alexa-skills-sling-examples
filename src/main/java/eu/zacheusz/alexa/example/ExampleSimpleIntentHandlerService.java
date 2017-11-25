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
package eu.zacheusz.alexa.example;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import eu.zacheusz.alexa.IntentHandler;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import static java.lang.String.format;

/**
 * @author zacheusz
 */
@Component
@Service(IntentHandler.class)
public class ExampleSimpleIntentHandlerService implements IntentHandler {

    private static final String SLOT_NAME = "mySlot";
    private static final String INTENT_NAME = "myIntent";

    @Override
    public boolean supportsIntent(String intentName) {
        return INTENT_NAME.equals(intentName);
    }

    @Override
    public SpeechletResponse handle(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

        final IntentRequest request = requestEnvelope.getRequest();
        final Intent intent = request.getIntent();
        final Slot slot = intent.getSlot(SLOT_NAME);

        final String responseMessage;
        if (slot == null) {
            responseMessage = format(
                    "I got your request, but there is no slot %",
                    SLOT_NAME);
        } else {
            responseMessage = format(
                    "I got your request. Slot value is %s. Thanks!",
                    slot.getValue());
        }

        return newTellResponse(responseMessage);
    }

    private SpeechletResponse newTellResponse(final String text) {
        return SpeechletResponse.newTellResponse(newPlainTextOutputSpeech(text));
    }

    /**
     * Factory method for retrieving an OutputSpeech object when given a string of TTS.
     *
     * @param text the text that should be spoken out to the user.
     * @return an instance of SpeechOutput.
     */
    private PlainTextOutputSpeech newPlainTextOutputSpeech(final String text) {
        final PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);
        return speech;
    }
}
