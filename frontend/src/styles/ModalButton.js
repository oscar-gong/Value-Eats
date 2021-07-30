import styled from "styled-components";
import { Button } from "@material-ui/core";

export const ModalButton = styled(Button)`
  background: white;
  border: 1px solid orange;
  color: #ff8e53;
  margin: 5px;
  &:hover {
    background: linear-gradient(45deg, #fe6b8b 30%, #ff8e53 90%);
    color: white;
  }
`;
