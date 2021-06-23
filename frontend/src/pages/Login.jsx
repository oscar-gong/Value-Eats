import React, { useState } from 'react';
import { FloatBox } from '../styles/FloatBox';
import { Title } from '../styles/Title';
import { AlignCenter } from '../styles/AlignCenter';
import { Box, TextField, Button } from '@material-ui/core';
import SendIcon from '@material-ui/icons/Send';

export default function Login () {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
        console.log("hello, " + username + " " + password);
        const loginResult = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {
                "Accept": "application/json", 
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "username": username,
                "password": password
            })
        })
        // const ans = await loginResult.json();
        console.log(loginResult);
    }

    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={4}>
                    <Title>Value Eats</Title>
                </Box>
                {/* <p>Disrupting the intersection between discount and advertising through centralisation</p> */}
                <Box pt={1}>
                    <TextField id="outlined-basic" label="Username" variant="outlined" onChange={e => setUsername(e.target.value)} value={username}/>
                </Box>
                <Box pt={2}>
                    <TextField type="password" id="outlined-basic" label="Password" variant="outlined" onChange={e => setPassword(e.target.value)} value={password}/>
                </Box>
                <Box pt={4} pb={4}>
                    <Button variant="contained"
                        color="primary"
                        endIcon={<SendIcon/>}
                        onClick={handleLogin}>
                        Log in
                    </Button>
                </Box>
                <Box height="30px" width="100%" display="flex" justifyContent="space-evenly" alignItems="center">
                    <h3>New to ValueEats?</h3>
                    <a href="#!">Sign up here</a>
                </Box>
                <a href="#!">Register as an eatery</a>
            </FloatBox>
        </AlignCenter>
    )
}