package org.cheeseandbacon.bitbeast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class BattleIo {
    private static final String TAG = BattleIo.class.getName();

    public static final char END_OF_MESSAGE = '\u0004';
    private static final int BATTLE_EXCHANGE_STAGE_1 = 0;
    private static final int BATTLE_EXCHANGE_STAGE_2 = 1;

    private BlockingQueue<String> queue;
    private boolean weAreServer;

    private Pet_Status pet_status;
    private Pet_Status them;
    private int our_seed;
    private int their_seed;

    public BattleIo (boolean weAreServer, Pet_Status pet_status, int our_seed) {
        this.weAreServer = weAreServer;
        this.pet_status = pet_status;
        this.our_seed = our_seed;

        them = null;
        their_seed = 0;
    }

    public void setQueue(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public BattleIoDataResult receiveMessage (Context context, String rawData) {
        // If the raw data exists and ends with the ASCII end of transmission character,
        // we know that the data is valid
        if (rawData.length() > 0 && rawData.charAt(rawData.length() - 1) == END_OF_MESSAGE) {
            ArrayList<String> data = new ArrayList<>();

            //Convert the bytes into strings
            String currentLine = "";

            for (int i = 0; i < rawData.length(); i++) {
                if (rawData.charAt(i) != '\n') {
                    currentLine += rawData.charAt(i);
                } else {
                    data.add(currentLine);
                    currentLine = "";
                }
            }

            if (data.size() == 0) {
                data.add(currentLine);
            }

            return processBattleData(context, data);
        } else {
            Log.e(TAG, "Battle data received was invalid");
        }

        return new BattleIoDataResult();
    }

    private BattleIoDataResult processBattleData (Context context, ArrayList<String> data) {
        BattleIoDataResult result = new BattleIoDataResult();

        int battleExchangeStage = Integer.parseInt(data.get(0).trim());
        data.remove(0);

        their_seed = Integer.parseInt(data.get(0).trim());
        data.remove(0);

        them = Pet_Status.createPetFromBattleData(data);

        Log.d(TAG, "Processing battle data, received seed: " + their_seed + ", received pet: " + them.name);

        if (battleExchangeStage == BATTLE_EXCHANGE_STAGE_1) {
            // We have just received a battle initiation

            Log.d(TAG, "Battle initiation received from " + them.name);

            // Send the instigating pet our battle data
            sendBattleData(BATTLE_EXCHANGE_STAGE_2);
        } else if (battleExchangeStage == BATTLE_EXCHANGE_STAGE_2) {
            // We have just received a battle response

            Log.d(TAG, "Battle response received from " + them.name);

            result.shouldEndConnection = true;
        } else {
            Log.e(TAG, "Invalid battleExchangeStage encountered while processing battle data: " + battleExchangeStage);

            result.shouldEndConnection = true;

            return result;
        }

        result.intent = new Intent(context, Activity_Battle.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean(context.getPackageName() + ".server", weAreServer);
        bundle.putBoolean(context.getPackageName() + ".shadow", false);
        bundle.putInt(context.getPackageName() + ".our_seed", our_seed);
        bundle.putInt(context.getPackageName() + ".their_seed", their_seed);
        bundle.putAll(them.write_bundle_battle_data(context.getPackageName()));

        result.intent.putExtras(bundle);

        return result;
    }

    public void beginBattle () {
        if (weAreServer) {
            // We are the server, so we first transmit our battle data to the client

            sendBattleData(BATTLE_EXCHANGE_STAGE_1);
        }
    }

    private void sendBattleData (int battleExchangeStage) {
        String battleStage = "battle initiation";

        if (battleExchangeStage == BATTLE_EXCHANGE_STAGE_2) {
            battleStage = "battle response";
        }

        Log.d(TAG, "Sending " + battleStage + ", our seed: " + our_seed + ", our pet: " + pet_status.name);

        String data = "";

        data += "" + battleExchangeStage + "\n";

        data += "" + our_seed + "\n";

        data += pet_status.getBattleData();

        // The final byte is the ASCII end of transmission character
        data += END_OF_MESSAGE;

        if (queue != null) {
            queue.offer(data);
        }
    }
}
