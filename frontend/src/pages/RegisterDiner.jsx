import React from 'react';
import { FloatBox } from '../styles/FloatBox';
import { Subtitle } from '../styles/Subtitle';
import { AlignCenter } from '../styles/AlignCenter';
import { Box, TextField, Button } from '@material-ui/core';
import SendIcon from '@material-ui/icons/Send';
import { Link } from 'react-router-dom';

export default function Register() {
    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={2}>
                    <Subtitle>Create Account</Subtitle>
                </Box>
                <Box pt={1} width="60%">
                    <TextField id="outlined-basic" label="Username" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Email Address" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Password" variant="outlined" fullWidth />
                </Box>
                <Box pt={2} width="60%">
                    <TextField id="outlined-basic" label="Confirm Password" variant="outlined" fullWidth />
                </Box>
                <Box pt={4} pb={4}>
                    <Button variant="contained"
                        color="primary"
                        endIcon={<SendIcon/>}>
                        Sign Up
                    </Button>
                </Box>
            </FloatBox>
        </AlignCenter>
    )
}