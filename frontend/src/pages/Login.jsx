import React, { useState } from "react";
import { FloatBox } from "../styles/FloatBox";
import { Title } from "../styles/Title";
import { AlignCenter } from "../styles/AlignCenter";
import { Box, TextField, Button } from "@material-ui/core";
import SendIcon from "@material-ui/icons/Send";
import { Link } from "react-router-dom";
import { useHistory, Redirect } from "react-router";
import { StoreContext } from "../utils/store";

export default function Login() {
    const history = useHistory();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const context = React.useContext(StoreContext);
    const [auth, setAuth] = context.auth;
    const [isDiner, setIsDiner] = context.isDiner;
    const setAlertOptions = context.alert[1];

    if (isDiner === "true" && auth !== null) return <Redirect to="/DinerLanding" />;
    if (isDiner === "false" && auth !== null) return <Redirect to="/EateryLanding" />;

    const handleLogin = async () => {
        console.log("hello, " + email + " " + password);
        const loginResponse = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: email,
                password: password,
            }),
        });
        // const ans = await loginResult.json();
        const loginData = await loginResponse.json();
        if (loginResponse.status === 200) {
            setAlertOptions({
                showAlert: true,
                variant: "success",
                message: loginData.message,
            });
            setAuth(loginData.data.token);
            localStorage.setItem('token', loginData.data.token);
            
            if (loginData.data.type === "diner") {
                setIsDiner("true");
                localStorage.setItem('isDiner', "true");
                console.log("diner found!");
                history.push("/DinerLanding");
            } else {
                setIsDiner("false");
                localStorage.setItem('isDiner', "false");
                history.push("/EateryLanding");
            }
        } else {
            setAlertOptions({
                showAlert: true,
                variant: "error",
                message: loginData.message,
            });
        }
    };

    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={4}>
                    <Title>Value Eats</Title>
                </Box>
                {/* <p>Disrupting the intersection between discount and advertising through centralisation</p> */}
                <Box pt={1} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Email address"
                        variant="outlined"
                        onChange={(e) => setEmail(e.target.value)}
                        value={email}
                        fullWidth
                    />
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        type="password"
                        id="outlined-basic"
                        label="Password"
                        variant="outlined"
                        onChange={(e) => setPassword(e.target.value)}
                        value={password}
                        fullWidth
                    />
                </Box>
                <Box pt={4} pb="8%">
                    <Button
                        variant="contained"
                        color="primary"
                        endIcon={<SendIcon />}
                        onClick={handleLogin}
                    >
                        Log in
                    </Button>
                </Box>
                <Box
                    height="30px"
                    width="100%"
                    display="flex"
                    justifyContent="space-evenly"
                    alignItems="center"
                >
                    <h3>New to ValueEats?</h3>
                    <Link to="/RegisterDiner">Sign up here</Link>
                </Box>
                <Box pb={4}>
                    <Link to="/RegisterEatery">Register as an eatery</Link>
                </Box>
            </FloatBox>
        </AlignCenter>
    );
}
