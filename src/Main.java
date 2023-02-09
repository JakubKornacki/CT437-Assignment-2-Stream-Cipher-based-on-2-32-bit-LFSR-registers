import java.util.HashMap;
import java.util.Map;

public class Main {
    private boolean[] register1;
    private boolean[] register2;

    private HashMap<String, Integer> occurances = new HashMap<String, Integer>();

    public static void main(String args[]) {
        if (args.length < 2 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty()) {
            System.out.println("You need to specify arguments: (32 bit binary string) (32 bit binary strings)");
        }
        Main main = new Main();
        String binaryString1 = args[0];
        String binaryString2 = args[1];
        int noOfRuns = Integer.parseInt(args[2]);
        main.start(binaryString1, binaryString2, noOfRuns);
    }

    private void start (String binaryString1, String binaryString2, int noOfRounds){
        // parse the binary strings into boolean array representation of bits
        try {
            register1 = parseBinaryStringToBooleanArray(binaryString1);
            register2 = parseBinaryStringToBooleanArray(binaryString2);
        } catch (InvalidInputParameter e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        // get the random sequence and get string representations of the registers and the random sequence
        boolean[] randomSequence = generateRandomSequence(noOfRounds);
        String randomSequenceAsString = booleanArrayToBinaryStringRepresentation(randomSequence);

        /* TEST 1: */
        //create the string summarizing the distribution of 1's and 0's
        String distribution = getDistributionOfBinaryString(randomSequenceAsString);
        System.out.println(distribution + "\n");

        /* TEST 2: */
        // get the longest runs of 0's and 1's for the binary sequence
        String longestRunsOnes = getLongestRuns(randomSequenceAsString, '1');
        String longestRunsZeros = getLongestRuns(randomSequenceAsString, '0');

        //System.out.println("Longest run of 1's: " + longestRunsOnes + "\twith length: " + longestRunsOnes.length());
        //System.out.println("Longest run of 0's: " + longestRunsZeros + "\twith length: " + longestRunsZeros.length());

        /* TEST 3: */
        countOccurances(randomSequenceAsString);
        // method to iterate through a hash map with both key and value at a time borrowed from
        // this post from StackOverflow: https://stackoverflow.com/a/8432400
        for(Map.Entry<String, Integer> entry : occurances.entrySet()) {
            int sequenceLength = entry.getKey().length();
            // print out the key value pairs that have the key length between 3 and 7
            if(sequenceLength > 3 && sequenceLength < 7) {
                System.out.println("Sequence: " + entry.getKey() + " occurred " + entry.getValue() + " times.");
            }
        }

    }

    /* TEST 3: */
    private void countOccurances(String binarySequence) {
        int counter1 = 0;
        int counter0 = 0;

        boolean flag0 = false, flag1 = false;
        // for the length of the binary sequence
        for(int i = 0; i < binarySequence.length(); i++) {

            // if this digit is 0, increment the counter and set flag0 to true (the sequence of zeros is now being counted)
            if(binarySequence.charAt(i) == '0') {
                counter0++;
                flag0 = true;
                // if the current digit is a 1, and we were counting zeros, save the substring in the hashmap and set flag0 to false
                // as 1's will be now counted and this is the end of the substring of 0's
            } else if (flag0) {
                // pass the length of this substring of zero's to the method that creates the hashmap key
                // and increments the counter for this substring
                // reset the flag to false since now 1's will be counter
                putOccurancesToHashMap(counter0, '0');
                counter0 = 0;
                flag0 = false;
            }

            if(binarySequence.charAt(i) == '1') {
                counter1++;
                flag1 = true;
                // if the current digit is a 0, and we were counting ones, save the substring in the hashmap and set flag1 to false
                // as 0's will be now counted and this is the end of the substring of 1's
            } else if (flag1) {
                // pass the length of this substring of zero's to the method that creates the hashmap key
                // and increments the counter for this substring
                // reset the flag to false since now 1's will be counter
                putOccurancesToHashMap(counter1, '1');
                counter1 = 0;
                flag1 = false;
            }
        }
    }

    /* TEST 3: */
    // if the count is not equal to zero, create the hashMapKey
    // if the map already contains the key, increment the Integer counter that can be accessed with this key,
    // otherwise create a entry with count set to 0
    private void putOccurancesToHashMap(int count, char digit) {
        if(count != 0) {
            String hashMapKey = generateHashMapKey(count, digit);
            if (occurances.containsKey(hashMapKey)) {
                int noOfOccurances = occurances.get(hashMapKey);
                occurances.put(hashMapKey, noOfOccurances + 1);
            } else {
                occurances.put(hashMapKey, 0);
            }
        }
    }

    /* TEST 3: */
    // create a string of length 'numberOfDigits' which is a sequence of the character passed as 'digit'
    private String generateHashMapKey(int numberOfDigits, char digit) {
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < numberOfDigits; i++) {
            temp.append(digit);
        }
        return temp.toString();
    }

    /* TEST 1: */
    // loop through the string representation of the binary sequence character at a time
    // throw an exception if the input string contains other characters than 1 or 0
    // return a string summarizing the distribution and the length of the binary sequence
    private String getDistributionOfBinaryString(String binarySequence) {
        int zeroCounter = 0;
        int oneCounter = 0;
        for(int i = 0; i < binarySequence.length(); i++) {
            if(binarySequence.charAt(i) == '1') {
                oneCounter++;
            } else if(binarySequence.charAt(i) == '0') {
                zeroCounter++;
            }
        }
        return "Number of 1's: " + oneCounter + "\nNumber of 0's: " + zeroCounter + "\nLength of binary sequence: " + binarySequence.length();
    }

    /* TEST 2: */
    private String getLongestRuns(String binarySequence, char binaryDigitToCheck) {
        StringBuilder temp = new StringBuilder();
        int longestRun = 0;
        int lengthOfSequence = 0;
        // loop through the binary sequence
        for(int i = 0; i < binarySequence.length(); i++) {
            // increment the counter for the current sequence of binary digit to check
            // if the sequence ended, check if the length of the sequence was greater
            // than the longest found up to this point
            // if yes, make this the longest sequence, if not, reset the counter
            if(binarySequence.charAt(i) == binaryDigitToCheck) {
                lengthOfSequence++;
            } else {
                if(longestRun < lengthOfSequence) {
                    longestRun = lengthOfSequence;
                }
                lengthOfSequence = 0;
            }
        }
        // check if the last sequence was the longest one
        if(lengthOfSequence > longestRun) {
            longestRun = lengthOfSequence;
        }

        // reconstruct the longest sequence by creating a string of length longestRun made out of the binary digit
        for(int i = 0; i < longestRun; i++) {
            temp.append(binaryDigitToCheck);
        }

        return temp.toString();
    }


    // core mechanism of generating the binary sequence
    private boolean[] generateRandomSequence(int noOfRounds) {
        boolean[] output = new boolean[noOfRounds];
        boolean majorityBit, LFSR1Bit, LFSR2Bit;

        // loop from the least significant bit to the most significant bit
        for(int i = noOfRounds-1; i > 0; i--) {
            // get bits from LFSR1 and LFSR2, XOR these bits to get the majorityBit
            LFSR1Bit = getBitFromLFSR1();
            LFSR2Bit = getBitFromLFSR2();
            majorityBit = LFSR1Bit ^ LFSR2Bit;

            // bit 8 (register1[24] chosen as the clocking bit of LFSR1
            // bit 5 (register2[27) chosen as the clocking bit of LFSR2
            // if the majority bit equals clocking bit of register1 or bit from LFSR2 is 1 => shift register1 to the left and insert LFSR1 bit to it
            if((majorityBit == register1[24])) {
                leftShiftRegister(register1, LFSR1Bit);
            }
            // if the majority bit equals clocking bit of register2 and bit from LFSR1 is 1 => shift register2 to the left and insert LFSR2 bit to it
            if((majorityBit == register2[27])) {
                leftShiftRegister(register2, LFSR2Bit);
            }
            // if both clocking bits agree, shift both registers to the left
            // this avoids the situation where both registers never get shifted to the left
            // when this happens the output is nearly only 0's or 1's
            if(register1[24] == register2[27]) {
                leftShiftRegister(register1, LFSR1Bit);
                leftShiftRegister(register2, LFSR2Bit);
            }
            // XOR two most significant bits together to get the output bit
            output[i] = register1[0] ^ register2[0];
        }
        return output;
    }



    // iterate through the string and create a binary representation of it using boolean values (true = 1 false = 0)
    // throw an InvalidInputParameter exception if the input contains invalid characters (other than 0 or 1)
    private boolean[] parseBinaryStringToBooleanArray(String binaryString) throws InvalidInputParameter {
        boolean[] binaryArray = new boolean[binaryString.length()];
        for(int i = 0; i < binaryString.length(); i++) {
           if(binaryString.charAt(i) == '0') {
               binaryArray[i] = false;
           } else if (binaryString.charAt(i) == '1') {
               binaryArray[i] = true;
           } else {
               throw new InvalidInputParameter("Input string contains invalid characters, it should only contains 0's or 1's!");
           }
        }
        return binaryArray;
    }
    // use bits 32,30, and 27 from register1 to create a new bit via the LFSR1 feedback function which does an XOR on these bits
    private boolean getBitFromLFSR1() {
        boolean bit32, bit30, bit27;
        bit32 = register1[0];
        bit30 = register1[2];
        bit27 = register1[5];
        // do a logical and on bit 32 nad bit 30, XOR the result with the NOT of bit27
        return (bit32 && bit30) ^ bit27;
    }

    // use bits 32,31,28 from register2 to create a new bit via the LFSR2 feedback function which does an XOR on these bits
    private boolean getBitFromLFSR2() {
        boolean bit32, bit31, bit28;
        bit32 = register2[0];
        bit31 = register2[1];
        bit28 = register2[4];
        // XOR bit 32 with NOT bit31 and XOR this result with bit28
        return (bit32 ^ !bit31) ^ bit28;
    }


    // shift all bits one place to the left starting from the most significant bit
    // set the least significant bit to be the new bit
    private void leftShiftRegister(boolean[] register, boolean bitToInsert) {
        for(int i = 0; i < register.length-1; i++) {
            register[i] = register[i+1];
        }
        register[register.length-1] = bitToInsert;
    }

    // loop through the boolean[] which represents the register and append either 1 or 0 to the output (1 if register[i] == true, 0 otherwise)
    private String booleanArrayToBinaryStringRepresentation(boolean[] register) {
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < register.length; i++) {
            if(register[i]) {
                temp.append('1');
            } else {
                temp.append('0');
            }
        }
        return temp.toString();
    }

}

