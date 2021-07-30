import styled, { keyframes } from "styled-components";
import { TextField } from "@material-ui/core";

const shakeHeadAnimation = keyframes`
   0% { left: 0px; }
  25% { left: 10px; }
  50% { left: -10px; }
  75% { left: 0px; }
`;

export const ShakeHead = styled(TextField)`
    animation-name: ${props => props.animate ? shakeHeadAnimation : ""};
    animation-duration: 0.8s;
`;
