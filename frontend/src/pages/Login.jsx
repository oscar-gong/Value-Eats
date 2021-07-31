import React, { useState } from "react";
import { Title } from "../styles/Title";
import { AlignCenter } from "../styles/AlignCenter";
import { Box, Button, Grid, makeStyles } from "@material-ui/core";
import { TextFieldStyled } from "../styles/TextFieldStyled";
import SendIcon from "@material-ui/icons/Send";
import { useHistory, Redirect } from "react-router";
import { StoreContext } from "../utils/store";
import { LinkStyled } from "../styles/LinkStyled";
import loginImage from "../assets/loginImage.jpeg";
import request from "../utils/request";

const useStyles = makeStyles({
  root: {
    background: "linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)",
  },
  gridItemLeft: {
    display: "flex",
    flexDirection: "column",
    width: "20vw",
    minWidth: "400px !important",
    alignItems: "center",
    height: "100%",
    backgroundColor: "white",
    borderRadius: "4% 0% 0% 4%",
    overflow: "hidden",
    boxShadow: "-6px 0 10px rgb(0 0 0 / 0.1)",
  },
  gridItemRight: {
    display: "flex",
    flexDirection: "column",
    width: "20vw",
    minWidth: "400px !important",
    alignItems: "center",
    height: "100%",
    backgroundColor: "white",
    borderRadius: "0% 4% 4% 0%",
    boxShadow: "6px 0 10px rgb(0 0 0 / 0.1)",
  },
  image: {
    objectFit: "cover",
    height: "100%",
    width: "100%",
  },
  mainContainer: {
    color: "#ff855b",
  },
});

export default function Login () {
  const history = useHistory();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const context = React.useContext(StoreContext);
  const [auth, setAuth] = context.auth;
  const [isDiner, setIsDiner] = context.isDiner;
  const setAlertOptions = context.alert[1];

  const classes = useStyles();

  if (isDiner === "true" && auth !== null) {
    return <Redirect to="/DinerLanding" />;
  }
  if (isDiner === "false" && auth !== null) {
    return <Redirect to="/EateryLanding" />;
  }

  const handleLogin = async () => {
    const payload = {
      email: email,
      password: password,
    };
    const loginResponse = await request.post("login", payload);
    const loginData = await loginResponse.json();
    if (loginResponse.status === 200) {
      setAlertOptions({
        showAlert: true,
        variant: "success",
        message: loginData.message,
      });
      setAuth(loginData.data.token);
      localStorage.setItem("token", loginData.data.token);

      if (loginData.data.type === "diner") {
        setIsDiner("true");
        localStorage.setItem("isDiner", "true");
        console.log("diner found!");
        history.push("/DinerLanding");
      } else {
        setIsDiner("false");
        localStorage.setItem("isDiner", "false");
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
      <Grid container justify="center" className={classes.mainContainer}>
        <Grid item>
          <div className={classes.gridItemLeft}>
            <img
              className={classes.image}
              src={loginImage}
              alt="graphic design of value eats"
            />
          </div>
        </Grid>
        <Grid item>
          <div className={classes.gridItemRight}>
            <Box pt={4}>
              <Title>Value Eats</Title>
            </Box>
            {/* <p>Disrupting the intersection between discount and advertising through centralisation</p> */}
            <Box pt={1} width="60%">
              <TextFieldStyled
                aria-label="outlined-basic"
                label="Email address"
                variant="outlined"
                onChange={(e) => setEmail(e.target.value)}
                value={email}
                fullWidth
              />
            </Box>
            <Box pt={2} width="60%">
              <TextFieldStyled
                aria-label="outlined-basic"
                type="password"
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
                className={classes.root}
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
              <LinkStyled to="/RegisterDiner">Sign up here</LinkStyled>
            </Box>
            <Box pb={4}>
              <LinkStyled to="/RegisterEatery">
                Register as an eatery
              </LinkStyled>
            </Box>
          </div>
        </Grid>
      </Grid>
    </AlignCenter>
  );
}
