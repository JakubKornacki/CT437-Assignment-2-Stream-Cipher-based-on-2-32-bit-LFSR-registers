public class Main {
    private boolean[] register1;
    private boolean[] register2;

    public static void main(String args[]) {
        if (args.length < 2 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty()) {
            System.out.println("You need to specify arguments: (32 bit binary string) (32 bit binary strings)");
        }
        Main main = new Main();
    }

    // iterate through the binary string and create a binary representation of it using boolean values true = 1 false = 0
    // return an empty array if the input contains other chars than 0 or 1
    private boolean[] parseStringBinaryInput(String binaryString) throws InvalidInputParameters {
        boolean[] binaryArray = new boolean[binaryString.length()];
        for(int i = 0; i < binaryString.length(); i++) {
           if(binaryString.charAt(i) == '0') {
               binaryArray[i] = false;
           } else if (binaryString.charAt(i) == '1') {
               binaryArray[i] = true;
           } else {
               throw new InvalidInputParameters("Input string contains invalid characters, it should only contains 0's or 1's!");
           }
        }
        return binaryArray;
    }

    // use bits 32,27,22,17,11 and 5 from register1 to create a new bit via the LFSR1 feedback function which does an XOR on these bits
    private boolean getBitFromLFSR1(String binaryString) {
        try {
            register1 = parseStringBinaryInput(binaryString);
        } catch (InvalidInputParameters e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        boolean bit32, bit27, bit22, bit17, bit11, bit5, newBit, outputBit;
        bit32 = register1[0];
        bit27 = register1[5];
        bit22 = register1[10];
        bit17 = register1[15];
        bit11 = register1[21];
        bit5 = register1[27];
        newBit = feedbackFunctionLFSR1(bit32, bit27, bit22, bit17, bit11, bit5);
        outputBit = register1[31];
        shiftRegister(register1, newBit);
        return outputBit;
    }


    // use bits 32,26,18,12,7 and 3 from register2 to create a new bit via the LFSR1 feedback function which does an XOR on these bits
    private boolean getBitFromLFSR2(String binaryString) {
        try {
            register2 = parseStringBinaryInput(binaryString);
        } catch (InvalidInputParameters e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        boolean bit32, bit26, bit18, bit12, bit7, bit3, newBit, outputBit;
        bit32 = register2[0];
        bit26 = register2[6];
        bit18 = register2[14];
        bit12 = register2[20];
        bit7 = register2[25];
        bit3 = register2[29];
        newBit = feedbackFunctionLFSR2(bit32, bit26, bit18, bit12, bit7, bit3);
        outputBit = register2[31];
        shiftRegister(register2, newBit);
        return outputBit;
    }

    // shift all bits one place to the right starting from the least significant bit
    // set the most significant bit to be the new bit
    private void shiftRegister(boolean[] register, boolean bit) {
        for(int i = register.length-1; i > 0; i--) {
            register[i] = register[i-1];
        }
        register[0] = bit;
    }

    // XOR together bits 32,26,18,12,7,3 to get a new bit
    private boolean feedbackFunctionLFSR2(boolean bit32, boolean bit26, boolean bit18, boolean bit12, boolean bit7, boolean bit3) {
        return bit32 ^ bit26 ^ bit18 ^ bit12 ^ bit7 ^ bit3;
    }

    // XOR together bits 32,27,22,17,11,5 to get a new bit
    private boolean feedbackFunctionLFSR1(boolean bit32, boolean bit27, boolean bit22, boolean bit17, boolean bit11, boolean bit5) {
        return bit32 ^ bit27 ^ bit22^ bit17 ^ bit11 ^ bit5;
    }

    // loop through the boolean[] which represents the register and append either 1 or 0 to the output (1 if register[i] == true, 0 otherwise)
    private String LFSRRegisterToString(boolean[] register) {
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

