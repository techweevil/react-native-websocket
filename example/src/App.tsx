import React, { useEffect } from 'react';

import { StyleSheet, View, Text, Button, Alert } from 'react-native';
import { multiply, wake, sleep } from 'react-native-websocket';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  useEffect(() => {
    multiply(3, 7).then(setResult);
  }, []);

  const handleWake = async () => {
    const msg = await wake();

    Alert.alert('Wake', `Result: ${msg}`);
  };

  const handleSleep = async () => {
    const msg = await sleep();

    Alert.alert('Sleep', `Result: ${msg}`);
  };

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Button title="Wake Up" onPress={handleWake} />
      <Button title="Sleep" onPress={handleSleep} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  button: {
    width: 150,
  },
});
