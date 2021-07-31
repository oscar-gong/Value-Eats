import React, { useState, useContext } from "react";
import { FloatBox } from "../styles/FloatBox";
import { Subtitle } from "../styles/Subtitle";
import { AlignCenter } from "../styles/AlignCenter";
import { Box } from "@material-ui/core";
import SendIcon from "@material-ui/icons/Send";
import AddPhotoAlternateIcon from "@material-ui/icons/AddPhotoAlternate";
import { useHistory, Redirect } from "react-router";
import {
  validRequired,
  validEmail,
  validConfirmPassword,
  validPassword,
  handleImage,
} from "../utils/helpers";
import { StoreContext } from "../utils/store";
import { ButtonStyled } from "../styles/ButtonStyle";
import { FileUpload } from "../styles/FileUpload";
import { ProfilePhoto } from "../styles/ProfilePhoto";
import { LinkStyled } from "../styles/LinkStyled";
import { TextFieldStyled } from "../styles/TextFieldStyled";
import request from "../utils/request";
import { Label } from "../styles/Label";

export default function RegisterDiner ({ setToken }) {
  const defaultState = { value: "", valid: true };
  const [username, setUsername] = useState(defaultState);
  const [email, setEmail] = useState(defaultState);
  const [password, setPassword] = useState(defaultState);
  const [confirmPassword, setConfirmPassword] = useState(defaultState);
  const history = useHistory();
  const context = useContext(StoreContext);
  const setAlertOptions = context.alert[1];
  const [auth, setAuth] = context.auth;
  const [isDiner, setIsDiner] = context.isDiner;
  const [tmpProfilePic, setTmpProfilePic] = useState(
    "https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg"
  );

  const registerDiner = async () => {
    // if sign up button is clicked with empty fields, show textfield error
    if (username.value === "") setUsername({ value: "", valid: false });
    if (email.value === "") setEmail({ value: "", valid: false });
    if (password.value === "") setPassword({ value: "", valid: false });
    if (confirmPassword.value === "") {
      setConfirmPassword({ value: "", valid: false });
    }

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
    ) {
      return;
    }

    console.log("registered");
    const payload = {
      alias: username.value,
      email: email.value,
      password: password.value,
      profilePic: tmpProfilePic,
    };
    const registerResponse = await request.post("register/diner", payload);
    const registerResult = await registerResponse.json();
    if (registerResponse.status === 200) {
      setAlertOptions({
        showAlert: true,
        variant: "success",
        message: registerResult.message,
      });
      setAuth(registerResult.data.token);
      localStorage.setItem("token", registerResult.data.token);
      setIsDiner("true");
      localStorage.setItem("isDiner", "true");
      history.push("/DinerLanding");
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

  if (isDiner === "true" && auth !== null) {
    return <Redirect to="/DinerLanding" />;
  }
  if (isDiner === "false" && auth !== null) {
    return <Redirect to="/EateryLanding" />;
  }

  return (
    <AlignCenter>
      <FloatBox display="flex" flexDirection="column" alignItems="center">
        <Box pt={2}>
          <Subtitle>Create Account</Subtitle>
        </Box>
        <Box pt={1} display="flex" width="70%" alignItems="center">
          <Box pr={1}>
            <Label style={{ border: "0px", padding: "0px" }}>
              <FileUpload
                type="file"
                accept="image/png, image/jpg, image/jpeg"
                onChange={(e) => handleImage(e.target.files, setTmpProfilePic)}
              />
              <Box position="relative" p={1}>
                <AddPhotoAlternateIcon
                  style={{
                    position: "absolute",
                    left: "65px",
                    bottom: "65px",
                    backgroundColor: "white",
                    zIndex: 5,
                  }}
                />
                <ProfilePhoto hover={true} size={70} src={tmpProfilePic} />
              </Box>
            </Label>
          </Box>
          <TextFieldStyled
            aria-label="outlined-basic"
            label="Username"
            onChange={(e) =>
              setUsername({ value: e.target.value, valid: true })
            }
            onBlur={() => validRequired(username, setUsername)}
            error={!username.valid}
            helperText={
              username.valid
                ? ""
                : "Please enter a username with less than 12 characters"
            }
            variant="outlined"
            fullWidth
          />
        </Box>
        {/* <Box pt={1} width="60%">

        </Box> */}
        <Box width="70%">
          <TextFieldStyled
            aria-label="outlined-basic"
            label="Email Address"
            onChange={(e) => setEmail({ value: e.target.value, valid: true })}
            onBlur={() => {
              console.log(email);
              validEmail(email, setEmail);
            }}
            error={!email.valid}
            helperText={email.valid ? "" : "Please enter a valid email"}
            variant="outlined"
            fullWidth
          />
        </Box>
        <Box pt={2} width="70%">
          <TextFieldStyled
            aria-label="outlined-basic"
            label="Password"
            type="password"
            onChange={(e) =>
              setPassword({ value: e.target.value, valid: true })
            }
            onBlur={() => validPassword(password, setPassword)}
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
        <Box py={2} width="70%">
          <TextFieldStyled
            aria-label="outlined-basic"
            label="Confirm Password"
            type="password"
            onChange={(e) =>
              setConfirmPassword({
                value: e.target.value,
                valid: true,
              })
            }
            onBlur={() =>
              validConfirmPassword(
                password,
                confirmPassword,
                setConfirmPassword
              )
            }
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
        <ButtonStyled
          widthPercentage={70}
          variant="contained"
          color="primary"
          endIcon={<SendIcon />}
          onKeyPress={handleKeyPress}
          onClick={registerDiner}
        >
          Sign Up
        </ButtonStyled>
        <Box pt={2} pb={4}>
          <LinkStyled to="/">Back to Login</LinkStyled>
        </Box>
      </FloatBox>
    </AlignCenter>
  );
}
