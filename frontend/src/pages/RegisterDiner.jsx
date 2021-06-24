import React from 'react';
import { FloatBox } from '../styles/FloatBox';
import { Subtitle } from '../styles/Subtitle';
import { AlignCenter } from '../styles/AlignCenter';
import { Box, TextField, Button } from '@material-ui/core';
import SendIcon from '@material-ui/icons/Send';

export default function RegisterDiner() {

    const defaultState = {value: "", valid: true};
    const [username, setUsername] = React.useState(defaultState);
    const [email, setEmail] = React.useState(defaultState);
    const [password, setPassword] = React.useState(defaultState);
    const [confirmPassword, setConfirmPassword] = React.useState(defaultState);

    const validUsername = () => {
        if (username.value === "") {
            setUsername({value: "", valid: false});
        }
    }

    const validPassword = () => {
        const regex =/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;
        if (!regex.test(password.value)) {
            setPassword({values: "", valid: false});
        }
    }

    const validConfirmPassword = () => {
        if (password.value !== confirmPassword.value) {
            setConfirmPassword({values: "", valid: false});
        }
    }
    
    const validEmail = () => {
        // temp regex
        const regex =/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!regex.test(email.value)) {
            setEmail({values: "", valid: false});
        }
    }

    const registerDiner = async() => {
        console.log("registering");
    

    }

    const handleKeyPress = e => {
        if (e.key === 'Enter') {
            registerDiner();
        }
    }

    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={2}>
                    <Subtitle>Create Account</Subtitle>
                </Box>
                <Box pt={1} width="60%">
                    <TextField id="outlined-basic" label="Username" onChange={e => setUsername({value: e.target.value, valid: true})} onBlur={validUsername} error={!username.valid} helperText={username.valid ? '' : 'Please enter a username'} variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Email Address" onChange={e => setEmail({value: e.target.value, valid: true})} onBlur={validEmail} error={!email.valid} helperText={email.valid ? '' : 'Please enter a valid email'} variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Password" onChange={e => setPassword({value: e.target.value, valid: true})} onBlur={validPassword} error={!password.valid} helperText={password.valid ? '' : 'Please enter a valid password with 1 lowercase, 1 upper case, 1 number with at least 8 characters'} variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Confirm Password" onChange={e => setConfirmPassword({value: e.target.value, valid: true})} onBlur={validConfirmPassword} error={!confirmPassword.valid} helperText={confirmPassword.valid ? '' : 'Please make sure your passwords match'} variant="outlined" fullWidth />
                </Box>
                <Box pt={4} pb={4}>
                    <Button variant="contained"
                        color="primary"
                        endIcon={<SendIcon/>} onKeyPress={handleKeyPress} onClick={registerDiner}>
                        Sign Up
                    </Button>
                </Box>
            </FloatBox>
        </AlignCenter>
    )
}