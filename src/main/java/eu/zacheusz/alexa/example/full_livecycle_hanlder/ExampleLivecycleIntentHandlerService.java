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
package eu.zacheusz.alexa.example.full_livecycle_hanlder;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import eu.zacheusz.alexa.handler.IntentHandler;
import eu.zacheusz.alexa.handler.LaunchHandler;
import eu.zacheusz.alexa.handler.SessionEndedHandler;
import eu.zacheusz.alexa.handler.SessionStartedHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author zacheusz
 */
@Component
@Service({IntentHandler.class, SessionStartedHandler.class, SessionEndedHandler.class, LaunchHandler.class})
public class ExampleLivecycleIntentHandlerService
        implements IntentHandler, SessionStartedHandler, SessionEndedHandler, LaunchHandler {

    @Property(unbounded = PropertyUnbounded.ARRAY, label = "Supported intents.",
            value = {"changeChannel"})
    private static final String INTENTS_PROPERTY = "intents";

    @Property(label = "Slot name.", value = "channel")
    private static final String SLOT_PROPERTY = "slot";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private Set<String> intents;
    private String slotName;

    @Activate
    protected final void activate(final Map<String, Object> properties) throws Exception {
        final Object slotProperty = properties.get(SLOT_PROPERTY);
        this.slotName = slotProperty == null ? null : slotProperty.toString();
        this.intents = Arrays.stream(PropertiesUtil.toStringArray(properties.get(INTENTS_PROPERTY)))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean supportsIntent(String intentName) {
        return this.intents.contains(intentName);
    }

    @Override
    public SpeechletResponse handleIntent(final SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

        final IntentRequest request = requestEnvelope.getRequest();
        final Intent intent = request.getIntent();
        final Slot slot = intent.getSlot(slotName);

        final String responseMessage;
        if (StringUtils.isNotBlank(slotName)) {
            if (slot == null) {
                responseMessage = format(
                        "I got your request, but there is no slot %.",
                        slotName);
            } else {
                responseMessage = format(
                        "Hey! I got your request. Slot value is %s.",
                        slot.getValue());
            }
        } else {
            responseMessage = "I got your request. No slots are configured in the handler.";
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

    @Override
    public void handleSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) {
        log.info("handling session started");
    }

    @Override
    public void handleSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {
        log.info("handling session ended");
    }

    @Override
    public SpeechletResponse handleLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        return newTellResponse("This is launch event.");
    }
}
