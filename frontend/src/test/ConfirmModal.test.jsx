import { shallow } from "enzyme";
import React from "react";
import renderer from "react-test-renderer";
import ConfirmModal from "../components/ConfirmModal";

describe("ConfirmModal", () => {
  const noop = () => {};
  it("can work in the base case", () => {
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
    />);
    expect(confirmWrapper.exists()).toBeTruthy();
  });

  it("handles default title and message are passed in correctly", () => {
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
    />);
    expect(confirmWrapper.text().includes("Test title")).toBe(true);
    expect(confirmWrapper.text().includes("Test description")).toBe(true);
    expect(confirmWrapper.text().includes("Test description that is not in")).toBe(false);
  });

  it("handles default confirm text correctly", () => {
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
    />);
    expect(confirmWrapper.text().includes("Confirm")).toBe(true);
  });

  it("handles default cancel text correctly", () => {
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
    />);
    expect(confirmWrapper.text().includes("Cancel")).toBe(true);
  });

  it("handles custom confirm text correctly", () => {
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
      confirmText={"This will confirm"}
    />);
    expect(confirmWrapper.text().includes("This will confirm")).toBe(true);
    expect(confirmWrapper.text().includes("Confirm")).toBe(false);
  });

  it("handles custom cancel text correctly", () => {
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
      denyText={"This will cancel"}
    />);
    expect(confirmWrapper.text().includes("This will cancel")).toBe(true);
    expect(confirmWrapper.text().includes("Cancel")).toBe(false);
  });

  it("handles close modal correctly", () => {
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
      denyText={"This will cancel"}
    />);
    expect(confirmWrapper.find("ConfirmModal").exists()).toBeFalsy();
  });

  it("handles close correctly when hitting close button in top right", () => {
    const onClick = jest.fn();
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={onClick}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
      denyText={"This will cancel"}
    />);
    confirmWrapper.simulate("close");
    expect(onClick).toHaveBeenCalledTimes(1);
  });

  it("handles cancel - default behaviour", () => {
    const onClick = jest.fn();
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={onClick}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
      denyText={"This will cancel"}
    />);
    const buttons = confirmWrapper.children().at(2).children().children();
    buttons.at(0).simulate("click");
    expect(onClick).toHaveBeenCalledTimes(1);
  });

  it("handles cancel - custom behaviour", () => {
    const onClick = jest.fn();
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
      handleDeny={onClick}
      denyText={"This will cancel"}
    />);
    const buttons = confirmWrapper.children().at(2).children().children();
    buttons.at(0).simulate("click");
    expect(onClick).toHaveBeenCalledTimes(1);
  });

  it("handles confirm", () => {
    const onClick = jest.fn();
    const confirmWrapper = shallow(<ConfirmModal open={true}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={onClick}
      denyText={"This will cancel"}
    />);
    const buttons = confirmWrapper.children().at(2).children().children();
    buttons.at(1).simulate("click");
    expect(onClick).toHaveBeenCalledTimes(1);
  });

  /* Snapshot tests - creates snapshots for new tests, otherwise will check the existing tests. To update existing tests,
  follow the prompts on the screen after running test. Done to avoid regression. */

  it("renders with minimal props", () => {
    const confirmWrapper = renderer.create(<ConfirmModal handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
    />);
    expect(confirmWrapper).toMatchSnapshot();
  });

  it("renders with custom confirm and deny text", () => {
    const confirmWrapper = renderer.create(<ConfirmModal handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
      confirmText={"Continue"}
      denyText={"Stop"}
    />);
    expect(confirmWrapper).toMatchSnapshot();
  });

  it("renders correctly when open is false", () => {
    const confirmWrapper = renderer.create(<ConfirmModal open={false}
      handleClose={noop}
      title={"Test title"}
      message={"Test description"}
      handleConfirm={noop}
    />);
    expect(confirmWrapper).toMatchSnapshot();
  });
});
