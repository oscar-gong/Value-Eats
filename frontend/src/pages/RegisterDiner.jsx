import React, { useState, useContext } from "react";
import { FloatBox } from "../styles/FloatBox";
import { Subtitle } from "../styles/Subtitle";
import { AlignCenter } from "../styles/AlignCenter";
import { Box, TextField, Button } from "@material-ui/core";
import SendIcon from "@material-ui/icons/Send";
import { usePlacesWidget } from "react-google-autocomplete";
import { useHistory } from "react-router";
import { checkValidEmail, checkValidPassword } from "../utils/helpers";
import { StoreContext } from "../utils/store";

// set to true for real demos
const useGoogleAPI = false;

export default function RegisterDiner({ setToken }) {
    const defaultState = { value: "", valid: true };
    const [username, setUsername] = useState(defaultState);
    const [email, setEmail] = useState(defaultState);
    const [password, setPassword] = useState(defaultState);
    const [confirmPassword, setConfirmPassword] = useState(defaultState);
    const [address, setAddress] = useState(defaultState);
    const history = useHistory();

    const context = useContext(StoreContext);
    const setAlertOptions = context.alert[1];

    const validUsername = () => {
        if (username.value === "") {
            console.log("not a valid username!");
            setUsername({ value: "", valid: false });
        }
    };

    const validPassword = () => {
        if (!checkValidPassword(password.value))
            setPassword({ values: "", valid: false });
    };

    const validConfirmPassword = () => {
        if (
            password.value !== confirmPassword.value ||
            confirmPassword.value === ""
        ) {
            setConfirmPassword({ values: "", valid: false });
        }
    };

    const validEmail = () => {
        if (!checkValidEmail(email.value))
            setEmail({ values: "", valid: false });
    };

    const validAddress = () => {
        if (address.value === "") {
            setAddress({ values: "", valid: false });
        }
    };

    const registerDiner = async () => {
        console.log(username, email, password, address);

        // if sign up button is clicked with empty fields, show textfield error
        if (username.value === "") setUsername({ value: "", valid: false });
        if (email.value === "") setEmail({ value: "", valid: false });
        if (password.value === "") setPassword({ value: "", valid: false });
        if (confirmPassword.value === "")
            setConfirmPassword({ value: "", valid: false });
        if (useGoogleAPI && address.value === "")
            setAddress({ value: "", valid: false });

        // check that all fields are valid and not empty before registering
        if (
            !username.valid ||
            !email.valid ||
            !password.valid ||
            !confirmPassword.valid ||
            username.value === "" ||
            email.value === "" ||
            password.value === "" ||
            confirmPassword.value === ""
        )
            return;
        if ((!address.valid || address.value === "") && useGoogleAPI) {
            return;
        }

        console.log("registered");
        console.log(username, email, password, address);
        const registerResponse = await fetch(
            "http://localhost:8080/register/diner",
            {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    alias: username.value,
                    email: email.value,
                    address: useGoogleAPI ? address.value : "Sydney",
                    password: password.value,
                }),
            }
        );
        const registerResult = await registerResponse.json();
        if (registerResponse.status === 200) {
            setAlertOptions({
                showAlert: true,
                variant: "success",
                message: registerResult.message,
            });
            setToken(registerResult.data.token);
            history.push("/dinerLanding");
        } else {
            setAlertOptions({
                showAlert: true,
                variant: "error",
                message: registerResult.message,
            });
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === "Enter") {
            registerDiner();
        }
    };

    const { ref } = usePlacesWidget({
        apiKey: "AIzaSyCG80LxbPTd4MNoZuPdzbF-aQA_DcCAGVQ",
        onPlaceSelected: (place) =>
            setAddress({ value: place.formatted_address, valid: true }),
        options: {
            types: ["address"],
            componentRestrictions: { country: "au" },
        },
    });

    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={2}>
                    <Subtitle>Create Account</Subtitle>
                </Box>
                <Box pt={1} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Username"
                        onChange={(e) =>
                            setUsername({ value: e.target.value, valid: true })
                        }
                        onBlur={validUsername}
                        error={!username.valid}
                        helperText={
                            username.valid ? "" : "Please enter a username"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Email Address"
                        onChange={(e) =>
                            setEmail({ value: e.target.value, valid: true })
                        }
                        onBlur={validEmail}
                        error={!email.valid}
                        helperText={
                            email.valid ? "" : "Please enter a valid email"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                {useGoogleAPI && (
                    <Box pt={2} width="60%">
                        <TextField
                            id="outlined-basic"
                            disabled={!useGoogleAPI}
                            onBlur={validAddress}
                            onChange={(e) =>
                                setAddress({
                                    value: e.target.value,
                                    valid: true,
                                })
                            }
                            error={!address.valid}
                            helperText={
                                address.valid ? "" : "Please enter an address"
                            }
                            fullWidth
                            color="secondary"
                            variant="outlined"
                            inputRef={ref}
                        />
                    </Box>
                )}

                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Password"
                        type="password"
                        onChange={(e) =>
                            setPassword({ value: e.target.value, valid: true })
                        }
                        onBlur={validPassword}
                        error={!password.valid}
                        helperText={
                            password.valid
                                ? ""
                                : "Please enter a valid password with 1 lowercase, 1 upper case, 1 number with at least 8 characters"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Confirm Password"
                        type="password"
                        onChange={(e) =>
                            setConfirmPassword({
                                value: e.target.value,
                                valid: true,
                            })
                        }
                        onBlur={validConfirmPassword}
                        error={!confirmPassword.valid}
                        helperText={
                            confirmPassword.valid
                                ? ""
                                : "Please make sure your passwords match"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                <Box pt={4} pb={4}>
                    <Button
                        variant="contained"
                        color="primary"
                        endIcon={<SendIcon />}
                        onKeyPress={handleKeyPress}
                        onClick={registerDiner}
                    >
                        Sign Up
                    </Button>
                </Box>
            </FloatBox>
        </AlignCenter>
    );
}
