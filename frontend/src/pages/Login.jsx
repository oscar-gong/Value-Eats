import React, { useState } from 'react';
import { FloatBox } from '../styles/FloatBox';
import { Title } from '../styles/Title';
import { AlignCenter } from '../styles/AlignCenter';
import { Box, TextField, Button } from '@material-ui/core';
import SendIcon from '@material-ui/icons/Send';
import { Link } from 'react-router-dom';
import { useHistory } from 'react-router';


export default function Login () {
    const history = useHistory();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
        console.log("hello, " + email + " " + password);
        const loginResult = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {
                "Accept": "application/json", 
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "email": email,
                "password": password
            })
        })
        // const ans = await loginResult.json();
        console.log(loginResult);
        alert(loginResult);
        if (loginResult.status === 200) {
            history.push("/dinerLanding");
        } else {
            alert("Login failed - username and password are incorrect");
        }
    }

    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={4}>
                    <Title>Value Eats</Title>
                </Box>
                {/* <p>Disrupting the intersection between discount and advertising through centralisation</p> */}
                <Box pt={1} width="60%">
                    <TextField id="outlined-basic" label="Email address" variant="outlined" onChange={e => setEmail(e.target.value)} value={email} fullWidth/>
                </Box>
                <Box pt={2}  width="60%">
                    <TextField type="password" id="outlined-basic" label="Password" variant="outlined" onChange={e => setPassword(e.target.value)} value={password} fullWidth/>
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
                    <Link to="/RegisterDiner">
                        Sign up here
                    </Link>
                </Box>
                <Link to="/RegisterEatery">
                    Register as an eatery
                </Link>
            </FloatBox>
        </AlignCenter>
    )
}